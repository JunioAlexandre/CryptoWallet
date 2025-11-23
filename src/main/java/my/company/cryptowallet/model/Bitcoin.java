package my.company.cryptowallet.model;

/**
 *
 * @author junio
 */

public class Bitcoin extends Criptomoeda {
    
    public Bitcoin() {
        super("BTC", "Bitcoin");
    }
    
    @Override
    public String getDescricaoCompleta() {
        return "Bitcoin (BTC) - A primeira e mais conhecida criptomoeda do mundo";
    }
}

