package my.company.cryptowallet.model;

/**
 *
 * @author junio
 */
/**
 * Classe que representa Ethereum
 * Demonstra: Herança (extends Criptomoeda) e Polimorfismo
 */
public class Ethereum extends Criptomoeda { //Herança
    
    public Ethereum() {
        super("ETH", "Ethereum");
    }
    
    @Override
    public String getDescricaoCompleta() { //Polimorfismo
        return "Ethereum (ETH) - Plataforma de contratos inteligentes e aplicativos descentralizados";
    }
}