package theknife.client.gui;

import java.io.IOException;
import java.sql.SQLException;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import theknife.Message;
import theknife.client.ClientManager;
import theknife.server.models.Guest;
import theknife.server.models.Luogo;
import theknife.server.models.Password;
import theknife.server.models.Utente;

public class Welcome implements GUIBasics {

    private Stage stage;
    private ClientManager client;

    public Welcome(Stage stage, ClientManager client) {
        this.stage = stage;
        this.client = client;
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
    }

    public void show() {
        stage.setScene(welcomeScene());
        stage.setTitle("TheKnife");
        stage.show();
    }

    private Scene makeScene(VBox box, double weight, double height) {
        StackPane container = new StackPane(box);
        StackPane.setAlignment(box, Pos.CENTER);
        Scene scene = new Scene(container, weight, height);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;
    }

    private Scene welcomeScene() {
        VBox layout = new VBox(16);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        Button loginBtn = GUIComponents.greenBtn("Accedi");
        Button guestBtn = GUIComponents.blackBtn("Accedi come Guest");

        loginBtn.setOnAction(e -> stage.setScene(loginScene()));
        guestBtn.setOnAction(e -> stage.setScene(guestScene()));

        Label noAcc = new Label("Non hai un account?");
        noAcc.setStyle("-fx-font-size:12;");
        Hyperlink regText = new Hyperlink("Registrati");
        regText.setOnAction(e -> stage.setScene(registerScene()));
        HBox reg = new HBox(4, noAcc, regText);
        reg.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(GUIComponents.logo(), loginBtn, guestBtn, reg);
        return makeScene(layout, WIDTH, HEIGHT);
    }

    private Scene loginScene() {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        TextField usernameField = GUIComponents.field("Username");
        usernameField.setMaxWidth(220);
        PasswordField passwordField = GUIComponents.passField("Password");
        passwordField.setMaxWidth(220);
        Label err = GUIComponents.errorLabel();

        Button loginBtn = GUIComponents.greenBtn("Accedi");
        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene()));

        loginBtn.setOnAction(e -> {
            GUIComponents.hideErr(err);
            String user = usernameField.getText().trim();
            String psw = passwordField.getText();
            if (user.isEmpty() || psw.isEmpty()) { GUIComponents.showError(err, "Tutti i campi devono essere completati."); return; }
            try {
                Message res = client.send(new Message("login", new Object[]{user, psw}));
                if (res.getOp().equals("OK")) {
                    new Home(stage, client, (Utente) res.getDati()[0]).show();
                } else {
                    GUIComponents.showError(err, "Credenziali errate. Riprova.");
                    passwordField.clear();
                }
            } catch (ClassNotFoundException | IOException | SQLException ioc) {
                GUIComponents.showError(err, "Connessione con il server fallita...");
            }
        });

        Label noAcc = new Label("Non hai un account?");
        noAcc.setStyle("-fx-font-size:12;");
        Hyperlink regText = new Hyperlink("Registrati");
        regText.setOnAction(e -> stage.setScene(registerScene()));
        HBox reg = new HBox(4, noAcc, regText);
        reg.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(GUIComponents.miniLogo(), usernameField, passwordField, err, loginBtn, backBtn, reg);
        return makeScene(layout, WIDTH, HEIGHT);
    }

    private Scene registerScene() {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        TextField nomeField = GUIComponents.field("Nome");
        TextField cognomeField = GUIComponents.field("Cognome");
        TextField usernameField = GUIComponents.field("Username");
        TextField domicilioField = GUIComponents.field("Domicilio (esempio: Viale Luigi Cadorna 10, Busto Arsizio, Italia)");
        PasswordField pswField = GUIComponents.passField("Password");
        PasswordField pswConfField = GUIComponents.passField("Conferma password");

        ToggleGroup ruoloBtns = new ToggleGroup();
        RadioButton ruolo_cliente = new RadioButton("Cliente");
        RadioButton ruolo_ristoratore = new RadioButton("Ristoratore");
        ruolo_cliente.setToggleGroup(ruoloBtns);
        ruolo_ristoratore.setToggleGroup(ruoloBtns);
        ruolo_cliente.setSelected(true);
        HBox ruoloRow = new HBox(20, ruolo_cliente, ruolo_ristoratore);
        ruoloRow.setAlignment(Pos.CENTER);

        Label err = GUIComponents.errorLabel();

        Button regBtn  = GUIComponents.greenBtn("Crea account");
        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene()));

        regBtn.setOnAction(e -> {
            GUIComponents.hideErr(err);
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String username = usernameField.getText().trim();
            String domicilio = domicilioField.getText().trim();
            String psw = pswField.getText();
            String pswConf = pswConfField.getText();
            String ruolo = ruolo_cliente.isSelected() ? "utente" : "ristoratore";

            if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || domicilio.isEmpty() || psw.isEmpty() || pswConf.isEmpty()) {
               GUIComponents.showError(err, "Tutti i campi devono essere completati."); return;
            }
            try {
                if(!Luogo.luogoExists(domicilio)) {
                    GUIComponents.showError(err, "Inserisci un indirizzo valido..."); return;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (!psw.equals(pswConf)) { GUIComponents.showError(err, "Le password non coincidono..."); return; }

            try {
                Message res = client.send(new Message("register", new Object[]{nome, cognome, username, Password.encrypt(psw), null, domicilio, ruolo}));
                if (res.getOp().equals("OK")) {
                    stage.setScene(loginScene());
                } else {
                    GUIComponents.showError(err, "Username già esistente.");
                }
            } catch (ClassNotFoundException | IOException ex) {
                GUIComponents.showError(err, "Errore di connessione al server.");
            }
        });

        layout.getChildren().addAll(GUIComponents.miniLogo(), nomeField, cognomeField, usernameField, domicilioField, pswField, pswConfField, ruoloRow, err, regBtn, backBtn);
        return GUIComponents.makeScene(layout, WIDTH, HEIGHT);
    }

    private Scene guestScene() {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        Label titleLabel = new Label("Inserisci il tuo domicilio:");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        TextField domicilioField = GUIComponents.field("Es: Via Roma, Milano, Italia");
        Label err = GUIComponents.errorLabel();

        Button continueBtn = GUIComponents.greenBtn("Continua");
        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene()));

        continueBtn.setOnAction(e -> {
            GUIComponents.hideErr(err);
            String domicilio = domicilioField.getText().trim();
            if (domicilio.isEmpty()) { GUIComponents.showError(err, "Inserisci un domicilio."); return; }
            try {
                if(!Luogo.luogoExists(domicilio)) {
                    GUIComponents.showError(err, "Inserisci un indirizzo valido..."); return;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }            
            try {
                new Home(stage, client, new Guest(domicilio)).show();
            } catch (SQLException | IOException ec) {
                GUIComponents.showError(err, "Errore durante il caricamento.");
            }
        });

        layout.getChildren().addAll(GUIComponents.miniLogo(), titleLabel, domicilioField, err, continueBtn, backBtn);
        return makeScene(layout, WIDTH, HEIGHT);
    }
}