package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import service.AlertService;
import service.EmailService;

import java.util.List;

public class MainController {
    @FXML
    private ListView<String> emailListView;

    @FXML
    public ProgressIndicator piCarga;

    private EmailService emailService;

    public MainController() {
        emailService = new EmailService();
    }

    @FXML
    private void onLoadEmailsClick() {
        piCarga.setVisible(true);
        emailListView.getItems().clear();

        Task<String[]> loadEmailsTask = new Task<String[]>() {
            @Override
            protected String[] call() throws Exception {
                return emailService.fetchEmails();
            }

            @Override
            protected void succeeded() {
                emailListView.getItems().addAll(getValue());
                piCarga.setVisible(false);
                AlertService.showAlert(Alert.AlertType.INFORMATION, "Éxito", "Emails cargados");
            }

            @Override
            protected void failed() {
                piCarga.setVisible(false);
                AlertService.showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar los correos");
            }
        };

        new Thread(loadEmailsTask).start();
    }

    @FXML
    private void onApplyLabelsClick() {
        piCarga.setVisible(true);
        emailListView.getItems().clear();

        Task<List<String>> applyLabelsTask = new Task<List<String>>() {
            @Override
            protected List<String> call() throws Exception {
                return emailService.labelEmails();
            }

            @Override
            protected void succeeded() {
                emailListView.getItems().addAll(getValue());
                piCarga.setVisible(false);
                AlertService.showAlert(Alert.AlertType.INFORMATION, "Éxito", "Etiquetas cargadas");
            }

            @Override
            protected void failed() {
                piCarga.setVisible(false);
                AlertService.showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las etiquetas");
            }
        };

        new Thread(applyLabelsTask).start();
    }
}