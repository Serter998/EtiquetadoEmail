package service;

import javafx.scene.control.Alert;

public class AlertService {

    public static void showAlert(Alert.AlertType tipoAlerta, String title, String content) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
