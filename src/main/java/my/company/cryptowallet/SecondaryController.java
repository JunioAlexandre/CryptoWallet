package my.company.cryptowallet;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.factory.CriptomoedaFactory;
import my.company.cryptowallet.model.Criptomoeda;
import my.company.cryptowallet.model.Transacao;
import my.company.cryptowallet.service.CriptoService;
import my.company.cryptowallet.util.SessaoUsuario;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller da Tela Principal/Dashboard (secondary.fxml)
 * Demonstra: TableView + ComboBox + Collections + Separação de Camadas
 */
public class SecondaryController {
    
    @FXML private Label lblUsuario;
    @FXML private Label lblPrecoBTC;
    @FXML private Label lblPrecoETH;
    @FXML private Label lblSaldoBTC;
    @FXML private Label lblSaldoETH;
    
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<String> cmbCripto;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPreco;
    
    @FXML private TableView<Transacao> tblTransacoes;
    @FXML private TableColumn<Transacao, String> colData;
    @FXML private TableColumn<Transacao, String> colTipo;
    @FXML private TableColumn<Transacao, String> colCripto;
    @FXML private TableColumn<Transacao, Double> colQuantidade;
    @FXML private TableColumn<Transacao, Double> colPreco;
    @FXML private TableColumn<Transacao, Double> colTotal;
    
    private CriptoService criptoService;
    private ObservableList<Transacao> transacoesObservable;
    
    // Armazena os preços atuais das criptomoedas
    private double precoAtualBTC = 0.0;
    private double precoAtualETH = 0.0;
    
    /**
     * Inicialização do controller
     * Demonstra: Collections (ObservableList) + ComboBox populado
     */
    @FXML
    public void initialize() {
        // Inicializa o serviço
        criptoService = new CriptoService();
        
        // Exibe o nome do usuário logado (Singleton)
        if (SessaoUsuario.getInstance().isLogado()) {
            lblUsuario.setText("Usuario: " + 
                SessaoUsuario.getInstance().getUsuarioLogado().getNomeCompleto());
        }
        
        // Popula os ComboBox (Controles Avançados)
        cmbTipo.setItems(FXCollections.observableArrayList("COMPRA", "VENDA"));
        cmbCripto.setItems(FXCollections.observableArrayList("BTC", "ETH"));
        
        // Configura promptText via Java (compatibilidade JavaFX 25)
        cmbTipo.setPromptText("Tipo");
        cmbCripto.setPromptText("Criptomoeda");
        txtQuantidade.setPromptText("Quantidade");
        txtPreco.setPromptText("Valor Total ($)");
        
        // Adiciona listener para calcular preço automaticamente
        txtQuantidade.textProperty().addListener((observable, oldValue, newValue) -> {
            calcularPrecoAutomatico();
        });
        
        cmbCripto.valueProperty().addListener((observable, oldValue, newValue) -> {
            calcularPrecoAutomatico();
        });
        
        // Configura as colunas da TableView
        colData.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDataFormatada()));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCripto.setCellValueFactory(new PropertyValueFactory<>("criptomoeda"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colTotal.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getValorTotal()).asObject());
        
        // Inicializa a lista observável
        transacoesObservable = FXCollections.observableArrayList();
        tblTransacoes.setItems(transacoesObservable);
        
        // Carrega preços iniciais
        handleAtualizarPrecos();
    }
    
    /**
     * Calcula automaticamente o valor total baseado na quantidade
     */
    private void calcularPrecoAutomatico() {
        try {
            // Verifica se foi selecionada uma criptomoeda
            if (cmbCripto.getValue() == null) {
                txtPreco.clear();
                return;
            }
            
            // Verifica se há quantidade informada
            String quantidadeText = txtQuantidade.getText();
            if (quantidadeText == null || quantidadeText.trim().isEmpty()) {
                txtPreco.clear();
                return;
            }
            
            // Converte a quantidade
            double quantidade = Double.parseDouble(quantidadeText);
            
            if (quantidade <= 0) {
                txtPreco.clear();
                return;
            }
            
            // Obtém o preço unitário da criptomoeda selecionada
            double precoUnitario = 0.0;
            String cripto = cmbCripto.getValue();
            
            if (cripto.equals("BTC")) {
                precoUnitario = precoAtualBTC;
            } else if (cripto.equals("ETH")) {
                precoUnitario = precoAtualETH;
            }
            
            // Verifica se o preço foi carregado
            if (precoUnitario == 0.0) {
                txtPreco.setText("0.00");
                return;
            }
            
            // Calcula o VALOR TOTAL (quantidade × preço unitário)
            double valorTotal = quantidade * precoUnitario;
            
            // Exibe o VALOR TOTAL no campo
            txtPreco.setText(String.format("%.2f", valorTotal));
            
        } catch (NumberFormatException e) {
            // Se houver erro na conversão, apenas limpa o campo
            txtPreco.clear();
        }
    }
    
    /**
     * Atualiza os preços via API
     * Demonstra: Separação de Camadas (Controller -> Service)
     */
    @FXML
    private void handleAtualizarPrecos() {
        try {
            // Chama o Service (Separação de Camadas)
            Criptomoeda[] criptos = criptoService.buscarPrecosAtualizados();
            
            // Armazena os preços nas variáveis
            precoAtualBTC = criptos[0].getPrecoAtual();
            precoAtualETH = criptos[1].getPrecoAtual();
            
            // Atualiza os labels NA THREAD DO JAVAFX
            javafx.application.Platform.runLater(() -> {
                lblPrecoBTC.setText(String.format("$ %.2f", precoAtualBTC));
                lblPrecoETH.setText(String.format("$ %.2f", precoAtualETH));
                
                // Atualiza os saldos
                atualizarSaldos();
                
                // Recalcula o preço se já houver quantidade informada
                calcularPrecoAutomatico();
            });
            
            System.out.println("Precos atualizados - BTC: " + precoAtualBTC + " ETH: " + precoAtualETH);
            
        } catch (RegraNegocioException e) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", 
                "Nao foi possivel atualizar os precos: " + e.getMessage());
        }
    }
    
    /**
     * Registra uma nova transação
     * Demonstra: Tratamento de Exceções + Validação
     */
    @FXML
    private void handleRegistrarTransacao() {
        try {
            // Validações básicas
            if (cmbTipo.getValue() == null || cmbCripto.getValue() == null) {
                throw new RegraNegocioException("Selecione o tipo e a criptomoeda!");
            }
            
            // Obtém a quantidade
            double quantidade = Double.parseDouble(txtQuantidade.getText());
            
            // Obtém o preço unitário correto
            double precoUnitario = 0.0;
            String cripto = cmbCripto.getValue();
            
            if (cripto.equals("BTC")) {
                precoUnitario = precoAtualBTC;
            } else if (cripto.equals("ETH")) {
                precoUnitario = precoAtualETH;
            }
            
            // Cria a transação com o preço unitário correto
            Transacao transacao = new Transacao();
            transacao.setTipo(cmbTipo.getValue());
            transacao.setCriptomoeda(cmbCripto.getValue());
            transacao.setQuantidade(quantidade);
            transacao.setPrecoUnitario(precoUnitario);
            
            // Registra via Service (Separação de Camadas)
            criptoService.registrarTransacao(transacao);
            
            // Atualiza a tabela (Collections)
            transacoesObservable.setAll(criptoService.listarTransacoes());
            
            // Atualiza saldos
            atualizarSaldos();
            
            // Limpa os campos
            limparCampos();
            
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", 
                "Transacao registrada com sucesso!");
            
        } catch (NumberFormatException e) {
            // Tratamento de erro de conversão
            exibirAlerta(Alert.AlertType.ERROR, "Erro", 
                "Quantidade e Preco devem ser numeros validos!");
        } catch (RegraNegocioException e) {
            // Tratamento da exceção customizada
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validacao", e.getMessage());
        }
    }
    
    /**
     * Navega para a tela de Gestão de Carteira (terceira tela)
     * Demonstra: Navegação entre telas
     */
    @FXML
    private void handleAbrirGestao() {
        try {
            App.setRoot("terceira");
        } catch (IOException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Navegacao", 
                "Nao foi possivel abrir a tela de Gestao: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza os labels de saldo
     */
    private void atualizarSaldos() {
        double saldoBTC = criptoService.calcularSaldo("BTC");
        double saldoETH = criptoService.calcularSaldo("ETH");
        
        lblSaldoBTC.setText(String.format("Saldo: %.4f BTC", saldoBTC));
        lblSaldoETH.setText(String.format("Saldo: %.4f ETH", saldoETH));
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        cmbTipo.setValue(null);
        cmbCripto.setValue(null);
        txtQuantidade.clear();
        txtPreco.clear();
    }
    
    /**
     * Faz logout e volta para a tela de login
     */
    @FXML
    private void handleSair() throws IOException {
        SessaoUsuario.getInstance().logout();
        App.setRoot("primary");
    }
    
    /**
     * Exibe um Alert
     */
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}