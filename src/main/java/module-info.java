module flores.rasillo.sergio.etiquetadoemail {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;

    opens flores.rasillo.sergio.etiquetadoemail to javafx.fxml;
    exports flores.rasillo.sergio.etiquetadoemail;

    opens controller to javafx.fxml;
    exports controller;
}