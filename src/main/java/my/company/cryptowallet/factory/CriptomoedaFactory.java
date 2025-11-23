package my.company.cryptowallet.factory;

import my.company.cryptowallet.model.Bitcoin;
import my.company.cryptowallet.model.Criptomoeda;
import my.company.cryptowallet.model.Ethereum;

/**
 *
 * @author junio
 */
/**
 * Factory Method para criar diferentes tipos de Criptomoedas
 * Demonstra: Padrão de Projeto Factory Method (Requisito Obrigatório)
 */
public class CriptomoedaFactory {
    
    /**
     * Cria uma instância de Criptomoeda baseado no símbolo
     * @param simbolo "BTC" ou "ETH"
     * @return Instância de Bitcoin ou Ethereum
     * @throws IllegalArgumentException se o símbolo for inválido
     */
    public static Criptomoeda criarCriptomoeda(String simbolo) {
        if (simbolo == null) {
            throw new IllegalArgumentException("Símbolo não pode ser nulo");
        }
        
        switch (simbolo.toUpperCase()) {
            case "BTC":
                return new Bitcoin();
            case "ETH":
                return new Ethereum();
            default:
                throw new IllegalArgumentException("Criptomoeda não suportada: " + simbolo);
        }
    }
    
    /**
     * Método auxiliar para obter todas as criptomoedas disponíveis
     * @return Array com todas as criptomoedas
     */
    public static Criptomoeda[] obterTodasCriptomoedas() {
        return new Criptomoeda[] {
            criarCriptomoeda("BTC"),
            criarCriptomoeda("ETH")
        };
    }
}