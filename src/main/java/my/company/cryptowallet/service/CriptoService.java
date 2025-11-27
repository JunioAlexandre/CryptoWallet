package my.company.cryptowallet.service;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.factory.CriptomoedaFactory;
import my.company.cryptowallet.model.Criptomoeda;
import my.company.cryptowallet.model.Transacao;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author junio
 */
/**
 * Serviço de gerenciamento de Criptomoedas e Transações Demonstra: Separação de
 * Camadas + Collections + Validações
 */
public class CriptoService {

    private List<Transacao> transacoes; //List para gerenciar transações
    private static final String API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd";

    public CriptoService() {
        this.transacoes = new ArrayList<>();
    }

    /**
     * Busca preços atualizados das criptomoedas via API
     *
     * @return Array com Bitcoin e Ethereum atualizados
     * @throws RegraNegocioException Se houver erro na API
     */
    //parte de criptoservice
    public Criptomoeda[] buscarPrecosAtualizados() throws RegraNegocioException {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RegraNegocioException("Erro ao buscar preços: HTTP " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parsear JSON
            JSONObject json = new JSONObject(response.toString());
            
            //usando o Factory
            Criptomoeda btc = CriptomoedaFactory.criarCriptomoeda("BTC");
            
            btc.setPrecoAtual(json.getJSONObject("bitcoin").getDouble("usd"));

            //Usando o Factory
            Criptomoeda eth = CriptomoedaFactory.criarCriptomoeda("ETH");
            
            eth.setPrecoAtual(json.getJSONObject("ethereum").getDouble("usd"));

            return new Criptomoeda[]{btc, eth};

        } catch (Exception e) {
            throw new RegraNegocioException("Erro ao buscar preços da API: " + e.getMessage());
        }
    }

    /**
     * Registra uma nova transação
     *
     * @param transacao Transação a ser registrada
     * @throws RegraNegocioException Se dados inválidos
     */
    //parte do CriptoService
    public void registrarTransacao(Transacao transacao) throws RegraNegocioException {
        // Validações
        if (transacao == null) {
            throw new RegraNegocioException("Transacao nao pode ser nula!");
        }

        if (transacao.getQuantidade() <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero!");
        }

        if (transacao.getPrecoUnitario() <= 0) {
            throw new RegraNegocioException("O preco unitario deve ser maior que zero!");
        }

        if (transacao.getTipo() == null || transacao.getTipo().trim().isEmpty()) {
            throw new RegraNegocioException("O tipo de transacao deve ser informado!");
        }

        transacoes.add(transacao);
    }

    /**
     * Lista todas as transações
     *
     * @return Lista de transações
     */
    public List<Transacao> listarTransacoes() {
        return new ArrayList<>(transacoes);
    }

    /**
     * Calcula o saldo total em uma criptomoeda específica
     *
     * @param simbolo "BTC" ou "ETH"
     * @return Quantidade total
     */
    public double calcularSaldo(String simbolo) {
        double saldo = 0.0;

        for (Transacao t : transacoes) {
            if (t.getCriptomoeda().equals(simbolo)) {
                if (t.getTipo().equals("COMPRA")) {
                    saldo += t.getQuantidade();
                } else if (t.getTipo().equals("VENDA")) {
                    saldo -= t.getQuantidade();
                }
            }
        }

        return saldo;
    }
}
