package my.company.cryptowallet.exception;

/**
 *
 * @author junio
 */
/**
 * Exceção customizada para violações de regras de negócio
 * Demonstra: Criação de Exceção Customizada (Requisito Obrigatório)
 */
public class RegraNegocioException extends Exception {
    
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
    
    public RegraNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}