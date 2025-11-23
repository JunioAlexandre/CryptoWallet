package my.company.cryptowallet.util;

import my.company.cryptowallet.model.Usuario;


/**
 *
 * @author junio
 */
/**
 * Singleton para gerenciar a sessão do usuário logado
 * Demonstra: Padrão Singleton (Requisito Opcional/Bônus)
 */
public class SessaoUsuario {
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;
    
    // Construtor privado (Singleton)
    private SessaoUsuario() {
    }
    
    /**
     * Obtém a instância única da sessão
     * @return Instância de SessaoUsuario
     */
    public static SessaoUsuario getInstance() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }
    
    /**
     * Define o usuário logado
     * @param usuario Usuário que fez login
     */
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
    /**
     * Obtém o usuário logado
     * @return Usuário logado ou null
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    /**
     * Verifica se há um usuário logado
     * @return true se houver usuário logado
     */
    public boolean isLogado() {
        return usuarioLogado != null;
    }
    
    /**
     * Encerra a sessão (logout)
     */
    public void logout() {
        this.usuarioLogado = null;
    }
}