package my.company.cryptowallet.model;

/**
 *
 * @author junio
 */
/**
 * Classe que representa um usuário do sistema
 * Demonstra: POO com Encapsulamento (atributos privados + getters/setters)
 */
public class Usuario {
    private String username;
    private String senha;
    private String nomeCompleto;
    
    // Construtores
    public Usuario() {
    }
    
    public Usuario(String username, String senha, String nomeCompleto) {
        this.username = username;
        this.senha = senha;
        this.nomeCompleto = nomeCompleto;
    }
    
    // Getters e Setters (Encapsulamento)
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getNomeCompleto() {
        return nomeCompleto;
    }
    
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
}