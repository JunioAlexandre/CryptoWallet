package my.company.cryptowallet;

import my.company.cryptowallet.factory.CriptomoedaFactory;
import my.company.cryptowallet.model.Criptomoeda;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 *
 * @author junio
 */
/**
 * Controller da Terceira Tela (Gestão de Carteira)
 * Demonstra: ListView + Factory Pattern + Polimorfismo
 */
public class TerceiraController {
    
    @FXML
    private Label lblResumo;
    
    @FXML
    private ListView<String> lstCriptomoedas;
    
    /**
     * Carrega as informações da carteira
     * Demonstra: Uso do Factory Pattern + Polimorfismo
     */
    @FXML
    private void handleCarregarInfo() {
        // Usa o Factory para criar as criptomoedas
        Criptomoeda[] criptos = CriptomoedaFactory.obterTodasCriptomoedas();
        
        // StringBuilder para construir o resumo
        StringBuilder resumo = new StringBuilder();
        resumo.append("Total de Criptomoedas Suportadas: ").append(criptos.length).append("\n\n");
        
        // Popula o ListView usando Polimorfismo
        ObservableList<String> items = FXCollections.observableArrayList();
        
        for (Criptomoeda cripto : criptos) {
            // Chama método abstrato (Polimorfismo)
            items.add(cripto.getSimbolo() + " - " + cripto.getNome());
            resumo.append("• ").append(cripto.getDescricaoCompleta()).append("\n");
        }
        
        lstCriptomoedas.setItems(items);
        lblResumo.setText(resumo.toString());
    }
    
    /**
     * Volta para o Dashboard
     */
    @FXML
    private void handleVoltar() throws IOException {
        App.setRoot("secondary");
    }
}