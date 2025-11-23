package my.company.cryptowallet.service;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.model.Transacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junio
 */
/**
 * Testes para CriptoService
 * Demonstra: Testes de validação e cálculos
 */
@DisplayName("Testes do Serviço de Criptomoedas")
public class CriptoServiceTest {
    
    private CriptoService service;
    
    @BeforeEach
    public void setUp() {
        service = new CriptoService();
    }
    
    /**
     * Teste: Registrar transação válida
     */
    @Test
    @DisplayName("Deve registrar transação válida com sucesso")
    public void testRegistrarTransacaoValida() throws RegraNegocioException {
        // Arrange
        Transacao transacao = new Transacao("COMPRA", "BTC", 0.5, 50000.0);
        
        // Act
        service.registrarTransacao(transacao);
        
        // Assert
        assertEquals(1, service.listarTransacoes().size(), 
            "Deve ter 1 transacao registrada");
    }
    
    /**
     * Teste: Registrar transação com quantidade zero
     */
    @Test
    @DisplayName("Deve lançar exceção para quantidade zero")
    public void testRegistrarTransacaoComQuantidadeZero() {
        // Arrange
        Transacao transacao = new Transacao("COMPRA", "BTC", 0, 50000.0);
        
        // Act & Assert
        RegraNegocioException exception = assertThrows(
            RegraNegocioException.class,
            () -> service.registrarTransacao(transacao),
            "Deveria lancar excecao para quantidade zero"
        );
        
        assertTrue(exception.getMessage().contains("maior que zero"));
    }
    
    /**
     * Teste: Registrar transação com preço negativo
     */
    @Test
    @DisplayName("Deve lançar exceção para preço negativo")
    public void testRegistrarTransacaoComPrecoNegativo() {
        // Arrange
        Transacao transacao = new Transacao("COMPRA", "BTC", 1.0, -50000.0);
        
        // Act & Assert
        RegraNegocioException exception = assertThrows(
            RegraNegocioException.class,
            () -> service.registrarTransacao(transacao),
            "Deveria lancar excecao para preco negativo"
        );
        
        assertTrue(exception.getMessage().contains("maior que zero"));
    }
    
    /**
     * Teste: Calcular saldo de Bitcoin
     */
    @Test
    @DisplayName("Deve calcular saldo correto de Bitcoin")
    public void testCalcularSaldoBitcoin() throws RegraNegocioException {
        // Arrange
        service.registrarTransacao(new Transacao("COMPRA", "BTC", 1.0, 50000.0));
        service.registrarTransacao(new Transacao("COMPRA", "BTC", 0.5, 51000.0));
        service.registrarTransacao(new Transacao("VENDA", "BTC", 0.3, 52000.0));
        
        // Act
        double saldo = service.calcularSaldo("BTC");
        
        // Assert
        assertEquals(1.2, saldo, 0.0001, "Saldo BTC deve ser 1.2");
    }
    
    /**
     * Teste: Calcular saldo de Ethereum
     */
    @Test
    @DisplayName("Deve calcular saldo correto de Ethereum")
    public void testCalcularSaldoEthereum() throws RegraNegocioException {
        // Arrange
        service.registrarTransacao(new Transacao("COMPRA", "ETH", 5.0, 3000.0));
        service.registrarTransacao(new Transacao("VENDA", "ETH", 2.0, 3200.0));
        
        // Act
        double saldo = service.calcularSaldo("ETH");
        
        // Assert
        assertEquals(3.0, saldo, 0.0001, "Saldo ETH deve ser 3.0");
    }
    
    /**
     * Teste: Saldo inicial deve ser zero
     */
    @Test
    @DisplayName("Deve retornar saldo zero quando não há transações")
    public void testSaldoInicialZero() {
        // Act
        double saldoBTC = service.calcularSaldo("BTC");
        double saldoETH = service.calcularSaldo("ETH");
        
        // Assert
        assertEquals(0.0, saldoBTC, "Saldo inicial BTC deve ser zero");
        assertEquals(0.0, saldoETH, "Saldo inicial ETH deve ser zero");
    }
    
    /**
     * Teste: Listar transações deve retornar lista vazia inicialmente
     */
    @Test
    @DisplayName("Deve retornar lista vazia quando não há transações")
    public void testListarTransacoesVazia() {
        // Act
        var transacoes = service.listarTransacoes();
        
        // Assert
        assertNotNull(transacoes, "Lista nao deve ser nula");
        assertTrue(transacoes.isEmpty(), "Lista deve estar vazia inicialmente");
    }
    
    /**
     * Teste: Registrar transação nula deve lançar exceção
     */
    @Test
    @DisplayName("Deve lançar exceção para transação nula")
    public void testRegistrarTransacaoNula() {
        // Act & Assert
        RegraNegocioException exception = assertThrows(
            RegraNegocioException.class,
            () -> service.registrarTransacao(null),
            "Deveria lancar excecao para transacao nula"
        );
        
        assertEquals("Transacao nao pode ser nula!", exception.getMessage());
    }
}

