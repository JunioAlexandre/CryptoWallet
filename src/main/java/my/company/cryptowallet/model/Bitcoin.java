package my.company.cryptowallet.model;

/**
 *
 * @author junio
 */

public class Bitcoin extends Criptomoeda { //Herança
    
    public Bitcoin() {
        super("BTC", "Bitcoin");
    }
    
    @Override
    public String getDescricaoCompleta() { //Polimorfismo
        return "Bitcoin (BTC) - A primeira e mais conhecida criptomoeda do mundo";
    }
}

