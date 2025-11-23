package my.company.cryptowallet.model;

/**
 *
 * @author junio
 */
/**
 * Classe abstrata que representa uma Criptomoeda
 * Demonstra: POO com Herança (será estendida por Bitcoin e Ethereum)
 */
public abstract class Criptomoeda {
    private String simbolo;
    private String nome;
    private double precoAtual;
    
    public Criptomoeda(String simbolo, String nome) {
        this.simbolo = simbolo;
        this.nome = nome;
        this.precoAtual = 0.0;
    }
    
    // Método abstrato (Polimorfismo)
    public abstract String getDescricaoCompleta();
    
    // Getters e Setters
    public String getSimbolo() {
        return simbolo;
    }
    
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public double getPrecoAtual() {
        return precoAtual;
    }
    
    public void setPrecoAtual(double precoAtual) {
        this.precoAtual = precoAtual;
    }
    
    @Override
    public String toString() {
        return simbolo + " - " + nome;
    }
}
