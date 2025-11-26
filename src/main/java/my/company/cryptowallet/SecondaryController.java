package my.company.cryptowallet;

import my.company.cryptowallet.exception.RegraNegocioException;
import my.company.cryptowallet.model.Criptomoeda;
import my.company.cryptowallet.model.Transacao;
import my.company.cryptowallet.service.CriptoService;
import my.company.cryptowallet.util.SessaoUsuario;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author junio
 */
/**
 * Controller da tela principal (secondary.fxml)
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
    @FXML private TableColumn<Transacao,String> colData;
    @FXML private TableColumn<Transacao,String> colTipo;
    @FXML private TableColumn<Transacao,String> colCripto;
    @FXML private TableColumn<Transacao,Double> colQuantidade;
    @FXML private TableColumn<Transacao,Double> colPreco;
    @FXML private TableColumn<Transacao,Double> colTotal;

    private CriptoService criptoService;
    private ObservableList<Transacao> transacoesObservable;

    // Preços carregados
    private double precoAtualBTC = 0.0;
    private double precoAtualETH = 0.0;

    @FXML
    public void initialize() {

        criptoService = new CriptoService();

        // Exibe nome do usuário
        if (SessaoUsuario.getInstance().isLogado()) {
            lblUsuario.setText(
                "Usuário: " + SessaoUsuario.getInstance().getUsuarioLogado().getNomeCompleto()
            );
        }

        // Preenche ComboBoxes
        cmbTipo.setItems(FXCollections.observableArrayList("COMPRA", "VENDA"));
        cmbCripto.setItems(FXCollections.observableArrayList("BTC", "ETH"));

        cmbTipo.setPromptText("Tipo");
        cmbCripto.setPromptText("Criptomoeda");
        txtQuantidade.setPromptText("Quantidade");
        txtPreco.setPromptText("Valor Total ($)");

        // Listeners
        txtQuantidade.textProperty().addListener((o,v,n)->calcularPrecoAutomatico());
        cmbCripto.valueProperty().addListener((o,v,n)->calcularPrecoAutomatico());

        // Configura colunas
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataFormatada()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colCripto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCriptomoeda()));
        colQuantidade.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getQuantidade()).asObject());
        colPreco.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecoUnitario()).asObject());
        colTotal.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getValorTotal()).asObject());

        // Formatadores de célula
        colQuantidade.setCellFactory(col -> new TableCell<Transacao,Double>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty||item==null ? null : String.format("%.8f", item));
            }
        });

        colPreco.setCellFactory(col -> new TableCell<Transacao,Double>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty||item==null ? null : String.format("$ %.2f", item));
            }
        });

        colTotal.setCellFactory(col -> new TableCell<Transacao,Double>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty||item==null ? null : String.format("$ %.2f", item));
            }
        });

        // Lista da tabela
        transacoesObservable = FXCollections.observableArrayList();
        tblTransacoes.setItems(transacoesObservable);

        carregarTransacoes();
        handleAtualizarPrecos();
    }

    private void carregarTransacoes() {
        transacoesObservable.setAll(criptoService.listarTransacoes());
    }

    private void calcularPrecoAutomatico() {
        try {
            if (cmbCripto.getValue() == null) { txtPreco.clear(); return; }
            if (txtQuantidade.getText().isBlank()) { txtPreco.clear(); return; }

            double qnt = Double.parseDouble(txtQuantidade.getText());
            double precoUnitario = cmbCripto.getValue().equals("BTC") ? precoAtualBTC : precoAtualETH;

            if (precoUnitario == 0) { txtPreco.setText("0.00"); return; }

            txtPreco.setText(String.format("%.2f", qnt * precoUnitario));

        } catch (Exception e) {
            txtPreco.clear();
        }
    }

    @FXML
    private void handleAtualizarPrecos() {
        try {
            Criptomoeda[] criptos = criptoService.buscarPrecosAtualizados();

            precoAtualBTC = criptos[0].getPrecoAtual();
            precoAtualETH = criptos[1].getPrecoAtual();

            Platform.runLater(() -> {
                lblPrecoBTC.setText(String.format("$ %.2f", precoAtualBTC));
                lblPrecoETH.setText(String.format("$ %.2f", precoAtualETH));
                atualizarSaldos();
                calcularPrecoAutomatico();
            });

        } catch (RegraNegocioException e) {
            exibirAlerta(Alert.AlertType.WARNING,"Aviso",
                    "Não foi possível atualizar os preços: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegistrarTransacao() {
        try {
            if (cmbTipo.getValue()==null || cmbCripto.getValue()==null)
                throw new RegraNegocioException("Selecione o tipo e a criptomoeda!");

            double quantidade = Double.parseDouble(txtQuantidade.getText());
            if (quantidade <= 0)
                throw new RegraNegocioException("A quantidade deve ser maior que zero!");

            String cripto = cmbCripto.getValue();
            double precoUnitario = cripto.equals("BTC") ? precoAtualBTC : precoAtualETH;

            if (precoUnitario == 0)
                throw new RegraNegocioException("Clique em 'Atualizar' antes de registrar!");

            // Cria transação
            Transacao t = new Transacao();
            t.setTipo(cmbTipo.getValue());
            t.setCriptomoeda(cripto);
            t.setQuantidade(quantidade);
            t.setPrecoUnitario(precoUnitario);

            criptoService.registrarTransacao(t);

           
            // 🔥 NOVO: salva dados da compra para o GRÁFICO
            if ("COMPRA".equalsIgnoreCase(t.getTipo())) {
                SessaoUsuario sessao = SessaoUsuario.getInstance();
                sessao.setCriptoSelecionada(t.getCriptomoeda());
                sessao.setPrecoCompra(t.getPrecoUnitario());
                sessao.setPrecoCompraTimestamp(System.currentTimeMillis());
            }

            carregarTransacoes();
            atualizarSaldos();
            limparCampos();

            exibirAlerta(Alert.AlertType.INFORMATION,"Sucesso",
                    "Transação registrada com sucesso!");

        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR,"Erro","Quantidade inválida!");
        } catch (RegraNegocioException e) {
            exibirAlerta(Alert.AlertType.ERROR,"Erro",e.getMessage());
        }
    }

    @FXML
    private void handleAbrirGestao() {
        try {
            App.setRoot("terceira");
        } catch (IOException e) {
            exibirAlerta(Alert.AlertType.ERROR,"Erro",
                    "Não foi possível abrir: " + e.getMessage());
        }
    }

    private void atualizarSaldos() {
        lblSaldoBTC.setText(String.format("Saldo: %.8f BTC", criptoService.calcularSaldo("BTC")));
        lblSaldoETH.setText(String.format("Saldo: %.8f ETH", criptoService.calcularSaldo("ETH")));
    }

    private void limparCampos() {
        cmbTipo.setValue(null);
        cmbCripto.setValue(null);
        txtQuantidade.clear();
        txtPreco.clear();
    }

    @FXML
    private void handleSair() throws IOException {
        SessaoUsuario.getInstance().logout();
        App.setRoot("primary");
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.showAndWait();
    }
}
