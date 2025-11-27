package my.company.cryptowallet;

import my.company.cryptowallet.factory.CriptomoedaFactory;
import my.company.cryptowallet.model.Criptomoeda;
import my.company.cryptowallet.util.SessaoUsuario;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 *
 * @author junio
 */
/**
 * Controller da Terceira Tela (Gestão de Carteira com gráfico)
 */

public class TerceiraController {

    @FXML private Label lblResumo;
    
    //ListView
    @FXML private ListView<String> lstCriptomoedas;

    // LineChart e eixos declarados no FXML
    @FXML private LineChart<Number, Number> graficoEvolucao;
    @FXML private NumberAxis eixoX;
    @FXML private NumberAxis eixoY;

    @FXML
    public void initialize() {
        // Deixa o gráfico limpo inicialmente
        if (graficoEvolucao != null) {
            graficoEvolucao.getData().clear();
            graficoEvolucao.setAnimated(true);
        }

        // Carrega lista de criptos (somente para exibir)
        carregarListaCriptomoedas();

        // Atualiza o gráfico caso já haja dados de compra na sessão
        carregarGraficoEvolucao();
    }

    /**
     * Preenche a lista e resumo com as criptomoedas disponíveis
     */
    private void carregarListaCriptomoedas() {
        Criptomoeda[] criptos = CriptomoedaFactory.obterTodasCriptomoedas();

        StringBuilder resumo = new StringBuilder();
        resumo.append("Total de Criptomoedas Suportadas: ").append(criptos.length).append("\n\n");
        
        //Populando dinamicamente
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Criptomoeda cripto : criptos) {
            items.add(cripto.getSimbolo() + " - " + cripto.getNome());
            resumo.append("• ").append(cripto.getDescricaoCompleta()).append("\n");
        }

        lstCriptomoedas.setItems(items);
        lblResumo.setText(resumo.toString());
    }

    /**
     * Monta o gráfico com o preço no momento da compra e o preço atual.
     * Se não houver compra registrada, limpa o gráfico e mostra mensagem.
     */
    private void carregarGraficoEvolucao() {
        SessaoUsuario sessao = SessaoUsuario.getInstance();

        graficoEvolucao.getData().clear();

        if (sessao.getCriptoSelecionada() == null || sessao.getPrecoCompraTimestamp() == 0L) {
            // Nenhuma compra registrada ainda
            lblResumo.setText(lblResumo.getText() + "\n\nNenhuma compra registrada para exibir no gráfico.");
            return;
        }

        String simbolo = sessao.getCriptoSelecionada();
        double precoCompra = sessao.getPrecoCompra();

        // Busca preço atual entre às criptos da factory
        double precoAtual = 0.0;
        Criptomoeda[] todas = CriptomoedaFactory.obterTodasCriptomoedas();
        for (Criptomoeda c : todas) {
            if (c.getSimbolo().equalsIgnoreCase(simbolo)) {
                precoAtual = c.getPrecoAtual();
                break;
            }
        }

        // Configura eixos e título
        graficoEvolucao.setTitle("Evolução: " + simbolo);
        eixoX.setLabel("Momento");
        eixoY.setLabel("Preço (USD)");

        // Cria série com 2 pontos (Compra -> Agora)
        XYChart.Series<Number, Number> serie = new XYChart.Series<>();
        serie.setName(simbolo);

        // Usamos X=0 para compra e X=1 para agora — é suficiente para mostrar a variação
        serie.getData().add(new XYChart.Data<>(0, precoCompra));
        serie.getData().add(new XYChart.Data<>(1, precoAtual));

        graficoEvolucao.getData().add(serie);

        // Atualiza o resumo com valores e indicação de valorização/desvalorização
        String status = precoAtual >= precoCompra ? "📈 Valorizou" : "📉 Desvalorizou";
        String resumo = String.format("Criptomoeda: %s\nPreço de compra: $ %.2f\nPreço atual: $ %.2f\n%s",
                simbolo, precoCompra, precoAtual, status);

        lblResumo.setText(resumo);
    }

    /**
     * Ação do botão "Carregar Informacoes" — força atualização do gráfico e lista
     */
    
    //Evento botão carregar
    @FXML
    private void handleCarregarInfo() {
        carregarListaCriptomoedas();
        carregarGraficoEvolucao();
    }

    //Navegar Gestão -> Dashboard e voltar
    @FXML
    private void handleVoltar() throws IOException {
        App.setRoot("secondary");
    }
}
