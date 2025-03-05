package service;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertService {

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
