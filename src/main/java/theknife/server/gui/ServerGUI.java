/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */

package theknife.server.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import theknife.client.gui.GUIComponents;
import theknife.server.ServerManager;

/**
 * Classe ServerGUI.
 * Implementa l'interfaccia grafica per avviare o stoppare il server.
 */
public class ServerGUI extends Application {

    /**
     * Larghezza predefinita dei field.
     */
    private final int FIELD_WIDTH = 350;

    /**
     * Larghezza predefinita dei bottoni.
     */    
    private final int BTN_WIDTH = 250;

    /**
     * Riferimento al server
     */
    private ServerManager serverManager;
    
    /**
     * Inizializza la GUI.
     *
     * @param stage {@link Stage}
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("TheKnife - Server");
        stage.setResizable(false);

        VBox layout = new VBox(14);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #1a1a1a;");

        TextField hostField = field("Host (es. localhost)");
        TextField portField = field("Porta (es. 5432)");
        TextField dbField   = field("Nome database (default: theknife)");
        TextField userField = field("Username");
        PasswordField pswField = passField("Password");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #888888;");
        statusLabel.setVisible(false);

        Button avvioBtn  = blackBtn("Avvia con credenziali");
        Button defaultBtn = greenBtn("Avvia default (SOLO TEST)");
        Button logoutBtn  = logoutButton();

        defaultBtn.setOnAction(e -> {
        statusLabel.setText("Avvio server in corso...");
        statusLabel.setVisible(true);
        defaultBtn.setDisable(true);
        avvioBtn.setDisable(true);            
            new Thread(() -> {
                try {
                    Platform.runLater(() -> statusLabel.setText("Server attivo sulla porta " + ServerManager.PORT));                    
                    serverManager = new ServerManager();
                    serverManager.exec();
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Errore: " + ex.getMessage());
                        defaultBtn.setDisable(false);
                        avvioBtn.setDisable(false);
                    });
                }
            }).start();
        });

        avvioBtn.setOnAction(e -> {
            if(hostField.getText().isEmpty() || portField.getText().isEmpty() || dbField.getText().isEmpty() || userField.getText().isEmpty() || pswField.getText().isEmpty()) {
                statusLabel.setText(("Inserisci tutti i campi...."));
                statusLabel.setVisible(true);
                return;
            }
            String url = "jdbc:postgresql://" + hostField.getText().trim() + ":" + portField.getText().trim() + "/" + dbField.getText().trim();
            statusLabel.setText("Avvio server in  corso...");
            statusLabel.setVisible(true);
            defaultBtn.setDisable(true);
            avvioBtn.setDisable(true);
            new Thread(() -> {
                try {
                    Platform.runLater(() -> statusLabel.setText("Server attivo sulla porta " + ServerManager.PORT));
                    ServerManager serverManager = new ServerManager(url, userField.getText().trim(), pswField.getText());
                    serverManager.exec();
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Errore: " + ex.getMessage());
                        defaultBtn.setDisable(false);
                        avvioBtn.setDisable(false);
                    });
                }
            }).start();
        });

        layout.getChildren().addAll(GUIComponents.logo(), hostField, portField, dbField, userField, pswField, defaultBtn, avvioBtn, logoutBtn, statusLabel);
        Scene scene = new Scene(layout, 420, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private TextField field(String text) {
        TextField f = new TextField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        f.setMaxWidth(FIELD_WIDTH);
        return f;
    }

    private PasswordField passField(String text) {
        PasswordField f = new PasswordField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        f.setMaxWidth(FIELD_WIDTH);
        return f;
    }

    private Button greenBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(BTN_WIDTH);
        b.getStyleClass().add("btn-green");
        return b;
    }

    private Button blackBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(BTN_WIDTH);
        b.getStyleClass().add("btn-black");
        return b;
    }

    private Button logoutButton() {
        Button btn = new Button("⛔ Esci");
        btn.getStyleClass().add("logout-btn");
        btn.setOnAction(e -> spegniServer());
        return btn;
    }  
    
    /**
     * Esegue le operazioni di chiusura del server.
     */
    private void spegniServer() {
            System.out.println("Chiusura del server in corso...");
            try {
                if (serverManager != null && serverManager.getServer() != null) {
                    serverManager.getServer().close(); //
                }
            } catch (Exception e) {
                System.err.println("Errore durante la chiusura del ServerSocket: " + e.getMessage());
            } finally {
                System.exit(0);
            }
        }    

    /**
     * Main di ServerGUI
     */
    public static void main(String[] args) {
        launch(args);
    }
}