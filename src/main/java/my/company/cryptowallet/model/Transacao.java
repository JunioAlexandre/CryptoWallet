package my.company.cryptowallet.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author junio
 */


/**
 * Classe que representa uma Transação (Compra ou Venda)
 * Demonstra: POO com Encapsulamento
 */
public class Transacao {
    private String tipo; 
    private String criptomoeda; 
    private double quantidade;
    private double precoUnitario;
    private LocalDateTime dataHora;
    
    public Transacao() {
        this.dataHora = LocalDateTime.now();
    }
    
    public Transacao(String tipo, String criptomoeda, double quantidade, double precoUnitario) {
        this.tipo = tipo;
        this.criptomoeda = criptomoeda;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.dataHora = LocalDateTime.now();
    }
    
    // Método para calcular o valor total da transação
    public double getValorTotal() {
        return quantidade * precoUnitario;
    }
    
    // Formatar data para exibição
    public String getDataFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataHora.format(formatter);
    }
    
    // Getters e Setters
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getCriptomoeda() {
        return criptomoeda;
    }
    
    public void setCriptomoeda(String criptomoeda) {
        this.criptomoeda = criptomoeda;
    }
    
    public double getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
    
    public double getPrecoUnitario() {
        return precoUnitario;
    }
    
    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}