package service;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import javafx.scene.control.Alert;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailService {

    private Store store;

    public EmailService() {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imap");
            properties.put("mail.imap.host", "imap.gmail.com");
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.ssl.trust", "imap.gmail.com");
            properties.put("mail.imap.ssl.enable", "false");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", 993, "sergio04.trabajo04@gmail.com", "jqgi vuww fpys ewzq");
        } catch (Exception e) {
            AlertService.showAlert(Alert.AlertType.ERROR, "Error", "Error al conectar al servidor de correo: " + e.getMessage());
            System.out.println("Error al conectar al servidor de correo: " + e.getMessage());
        }
    }

    public String[] fetchEmails() {
        try {
            Folder inbox = store.getDefaultFolder().getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            String[] emailSubjects = new String[Math.min(7, messages.length)];
            for (int i = 0; i < emailSubjects.length; i++) {
                emailSubjects[i] = "Correo: " + messages[i].getSubject();
            }
            inbox.close(false);
            return emailSubjects;
        } catch (Exception e) {
            AlertService.showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar los correos: " + e.getMessage());
            System.out.println("Error al cargar los correos: " + e.getMessage());
            return new String[]{"Error al cargar correos"};
        }
    }

    public List<String> labelEmails() {
        List<String> emailEtiquetado = new ArrayList<>();
        try {
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();

            int limit = Math.min(7, messages.length);

            String[] folderNames = {"Hecho", "En progreso", "Por hacer"};
            Folder[] folders = new Folder[folderNames.length];

            for (int i = 0; i < folderNames.length; i++) {
                folders[i] = store.getFolder(folderNames[i]);
                if (!folders[i].exists()) {
                    folders[i].create(Folder.HOLDS_MESSAGES);
                }
            }

            for (int i = 0; i < limit; i++) {
                Message message = messages[i];
                List<String> emailLabels = new ArrayList<>();

                for (Folder folder : folders) {
                    if (folder.exists() && folder.getMessageCount() > 0) {
                        if (isMessageInFolder(message, folder)) {
                            emailLabels.add(folder.getName());
                        }
                    }
                }

                if (!emailLabels.isEmpty()) {
                    emailEtiquetado.add("Correo: " + message.getSubject() + " | Etiquetas: " + String.join(", ", emailLabels));
                } else {
                    emailEtiquetado.add("Correo: " + message.getSubject());
                }
            }
            inbox.close(false);
        } catch (Exception e) {
            emailEtiquetado = new ArrayList<>();
            emailEtiquetado.add("Error al cargar los correos: " + e.getMessage());
            AlertService.showAlert(Alert.AlertType.ERROR, "Error", "Error al obtener las etiquetas: " + e.getMessage());
            System.out.println("Error al obtener las etiquetas: " + e.getMessage());
        }

        return emailEtiquetado;
    }

    private boolean isMessageInFolder(Message message, Folder folder) {
        try {
            if (folder instanceof IMAPFolder imapFolder) {
                imapFolder.open(Folder.READ_ONLY);
                String messageUID = ((IMAPMessage) message).getMessageID();
                Message[] folderMessages = imapFolder.getMessages();

                for (Message folderMessage : folderMessages) {
                    String folderMessageUID = ((IMAPMessage) folderMessage).getMessageID();
                    if (messageUID.equals(folderMessageUID)) {
                        imapFolder.close(false);
                        return true;
                    }
                }
                imapFolder.close(false);
            }
        } catch (Exception e) {
            System.out.println("Error al verificar si el mensaje está en la carpeta: " + e.getMessage());
        }
        return false;
    }
}
