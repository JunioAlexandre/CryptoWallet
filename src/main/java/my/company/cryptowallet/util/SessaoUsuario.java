package my.company.cryptowallet.util;

import my.company.cryptowallet.model.Usuario;

/**
 *
 * @author junio
 */
/**
 * Singleton para gerenciar a sessão do usuário logado
 */
public class SessaoUsuario {
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;
    private String criptoSelecionada;
    private double precoCompra;
    private long precoCompraTimestamp; // milissegundos desde epoch

    // Construtor privado (Singleton)
    private SessaoUsuario() { }

    public static SessaoUsuario getInstance() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }

    public void logout() {
        this.usuarioLogado = null;
        this.criptoSelecionada = null;
        this.precoCompra = 0.0;
        this.precoCompraTimestamp = 0L;
    }

    
    public void setCriptoSelecionada(String cripto) {
        this.criptoSelecionada = cripto;
    }

    public String getCriptoSelecionada() {
        return criptoSelecionada;
    }

    public void setPrecoCompra(double preco) {
        this.precoCompra = preco;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompraTimestamp(long timestampMillis) {
        this.precoCompraTimestamp = timestampMillis;
    }

    public long getPrecoCompraTimestamp() {
        return precoCompraTimestamp;
    }
}
