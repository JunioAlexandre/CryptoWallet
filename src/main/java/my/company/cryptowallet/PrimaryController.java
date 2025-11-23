package my.company.cryptowallet;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.model.Usuario;
import my.company.cryptowallet.service.AutenticacaoService;
import my.company.cryptowallet.util.SessaoUsuario;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller da Tela de Login (primary.fxml)
 * Demonstra: Controller + FXML + Navegação + Tratamento de Exceções
 */
public class PrimaryController {
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtSenha;
    
    private AutenticacaoService autenticacaoService;
    
    /**
     * Inicialização do controller
     */
    @FXML
    public void initialize() {
        // Inicializa o serviço (Separação de Camadas)
        autenticacaoService = new AutenticacaoService();
    }
    
    /**
     * Manipulador de eventos - Botão Login
     * Demonstra: Manipulação de Eventos + Tratamento de Exceções
     */
    @FXML
    private void handleLogin() {
        try {
            // Obtém os dados do formulário
            String username = txtUsername.getText();
            String senha = txtSenha.getText();
            
            // Chama o Service (Separação de Camadas)
            Usuario usuario = autenticacaoService.autenticar(username, senha);
            
            // Armazena na sessão (Singleton)
            SessaoUsuario.getInstance().setUsuarioLogado(usuario);
            
            // Feedback ao usuário (Alert de Sucesso)
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", 
                "Bem-vindo, " + usuario.getNomeCompleto() + "!");
            
            // Navega para a tela principal
            navegarParaSecondary();
            
        } catch (RegraNegocioException e) {
            // Tratamento da exceção customizada
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Login", e.getMessage());
        } catch (NumberFormatException e) {
            // Tratamento de erro de conversão
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Formato de dados inválido!");
        } catch (Exception e) {
            // Tratamento genérico
            exibirAlerta(Alert.AlertType.ERROR, "Erro Inesperado", 
                "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    /**
     * Navega para a tela secundária (Dashboard)
     * Demonstra: Navegação entre telas
     */
    private void navegarParaSecondary() throws IOException {
        App.setRoot("secondary");
    }
    
    /**
     * Exibe um Alert (Caixa de Diálogo)
     * Demonstra: Feedback ao Usuário
     */
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}