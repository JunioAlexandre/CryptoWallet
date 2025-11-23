package my.company.cryptowallet.service;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.model.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junio
 */
/**
 * Serviço de Autenticação Demonstra: Separação de Camadas (Service) +
 * Lançamento de Exceção Customizada
 */
public class AutenticacaoService {

    private List<Usuario> usuarios;

    public AutenticacaoService() {
        this.usuarios = new ArrayList<>();
        inicializarUsuariosPadrao();
    }

    /**
     * Inicializa usuários padrão para teste
     */
    private void inicializarUsuariosPadrao() {
        usuarios.add(new Usuario("admin", "1234", "Administrador do Sistema"));
        usuarios.add(new Usuario("user", "user123", "Usuário Teste"));
    }

    /**
     * Realiza o login do usuário
     *
     * @param username Nome de usuário
     * @param senha Senha
     * @return Usuario autenticado
     * @throws RegraNegocioException Se credenciais inválidas
     */
    public Usuario autenticar(String username, String senha) throws RegraNegocioException {
        // Validações
        if (username == null || username.trim().isEmpty()) {
            throw new RegraNegocioException("O nome de usuario nao pode estar vazio!");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new RegraNegocioException("A senha nao pode estar vazia!");
        }

        // Busca o usuário
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }

        // Se não encontrou, lança exceção
        throw new RegraNegocioException("Usuario ou senha incorretos!");
    }

    /**
     * Obtém todos os usuários cadastrados
     *
     * @return Lista de usuários
     */
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

}
