package my.company.cryptowallet.service;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junio
 */
/**
 * Testes de Unidade para AutenticacaoService
 * Demonstra: JUnit + Asserts + Teste de Exceções
 */
@DisplayName("Testes do Serviço de Autenticação")
public class AutenticacaoServiceTest {
    
    private AutenticacaoService service;
    
    /**
     * Configuração executada antes de cada teste
     */
    @BeforeEach
    public void setUp() {
        service = new AutenticacaoService();
    }
    
    /**
     * Teste: Autenticação com credenciais válidas
     * Demonstra: assertEquals
     */
    @Test
    @DisplayName("Deve autenticar usuário com credenciais válidas")
    public void testAutenticacaoComCredenciaisValidas() throws RegraNegocioException {
        // Arrange (Preparar)
        String username = "admin";
        String senha = "1234";
        
        // Act (Executar)
        Usuario usuario = service.autenticar(username, senha);
        
        // Assert (Verificar)
        assertNotNull(usuario, "Usuario nao deve ser nulo");
        assertEquals("admin", usuario.getUsername(), "Username deve ser 'admin'");
        assertEquals("Administrador do Sistema", usuario.getNomeCompleto(), 
            "Nome completo deve corresponder");
    }
    
    /**
     * Teste: Autenticação com credenciais inválidas
     * Demonstra: assertThrows (teste de exceção)
     */
    @Test
    @DisplayName("Deve lançar exceção com credenciais inválidas")
    public void testAutenticacaoComCredenciaisInvalidas() {
        // Arrange
        String username = "usuario_invalido";
        String senha = "senha_errada";
        
        // Act & Assert
        RegraNegocioException exception = assertThrows(
            RegraNegocioException.class,
            () -> service.autenticar(username, senha),
            "Deveria lancar RegraNegocioException para credenciais invalidas"
        );
        
        // Verifica a mensagem da exceção
        assertEquals("Usuario ou senha incorretos!", exception.getMessage());
    }
    
    /**
     * Teste: Autenticação com username vazio
     * Demonstra: assertThrows (teste de validação)
     */
    @Test
    @DisplayName("Deve lançar exceção com username vazio")
    public void testAutenticacaoComUsernameVazio() {
        // Arrange
        String username = "";
        String senha = "1234";
        
        // Act & Assert
        RegraNegocioException exception = assertThrows(
            RegraNegocioException.class,
            () -> service.autenticar(username, senha),
            "Deveria lancar excecao para username vazio"
        );
        
        assertTrue(exception.getMessage().contains("usuario nao pode estar vazio"),
            "Mensagem deve mencionar que username esta vazio");
    }
    
    /**
     * Teste: Autenticação com senha nula
     * Demonstra: assertThrows (teste de validação)
     */
    @Test
    @DisplayName("Deve lançar exceção com senha nula")
    public void testAutenticacaoComSenhaNula() {
        // Arrange
        String username = "admin";
        String senha = null;
        
        // Act & Assert
        assertThrows(
            RegraNegocioException.class,
            () -> service.autenticar(username, senha),
            "Deveria lancar excecao para senha nula"
        );
    }
    
    /**
     * Teste: Listar usuários
     * Demonstra: assertTrue + Collections
     */
    @Test
    @DisplayName("Deve listar todos os usuários cadastrados")
    public void testListarUsuarios() {
        // Act
        var usuarios = service.listarUsuarios();
        
        // Assert
        assertNotNull(usuarios, "Lista nao deve ser nula");
        assertFalse(usuarios.isEmpty(), "Lista nao deve estar vazia");
        assertTrue(usuarios.size() >= 2, "Deve ter pelo menos 2 usuarios padrao");
    }
}
