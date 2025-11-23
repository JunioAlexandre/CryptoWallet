module my.company.cryptowallet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens my.company.cryptowallet to javafx.fxml;
    exports my.company.cryptowallet;
}
