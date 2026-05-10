package theknife.client.gui;

import java.io.IOException;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import theknife.Message;
import theknife.client.ClientManager;
import theknife.server.models.Password;
import theknife.server.models.Utente;

public class Welcome {

    private static final double WIDTH     = 960;
    private static final double HEIGHT    = 540;
    private static final double MIN_WIDTH  = 600;
    private static final double MIN_HEIGHT = 400;
    private static final double FIELD_WIDTH = 150;

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

    private TextField field(String text) {
        TextField f = new TextField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        return f;
    }

    private PasswordField passField(String text) {
        PasswordField f = new PasswordField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        return f;
    }

    private Button greenBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(FIELD_WIDTH);
        b.getStyleClass().add("btn-green");
        return b;
    }

    private Button blackBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(FIELD_WIDTH);
        b.getStyleClass().add("btn-black");
        return b;
    }

    private Label errorLabel() {
        Label l = new Label();
        l.getStyleClass().add("error-label");
        l.setVisible(false);
        l.setManaged(false);
        return l;
    }

    private void showError(Label l, String msg) {
        l.setText("Errore: " + msg);
        l.setVisible(true);
        l.setManaged(true);
    }

    private void hideErr(Label l) {
        l.setVisible(false);
        l.setManaged(false);
    }

    private VBox logo() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        try {
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
            img.setFitHeight(70);
            img.setPreserveRatio(true);
            box.getChildren().add(img);
        } catch (Exception ignored) {}
        return box;
    }

    //Scene
    private Scene welcomeScene() {
        VBox layout = new VBox(16);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        Button loginBtn = greenBtn("Accedi come Utente");
        Button guestBtn = blackBtn("Accedi come Guest");

        loginBtn.setOnAction(e -> stage.setScene(loginScene()));
        // vai home "guest"

        Label noAcc = new Label("Non hai un account?");
        noAcc.setStyle("-fx-font-size:12;");
        Hyperlink regText = new Hyperlink("Registrati");
        regText.setOnAction(e -> stage.setScene(registerScene()));
        HBox reg = new HBox(4, noAcc, regText);
        reg.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(logo(), loginBtn, guestBtn, reg);
        return makeScene(layout, WIDTH, HEIGHT);
    }

    private Scene loginScene() {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        Label title = new Label("Accedi");
        title.setStyle("-fx-font-size:22; -fx-font-weight:bold;");

        TextField usernameField     = field("Username");
        PasswordField passwordField = passField("Password");
        Label err = errorLabel();

        Button loginBtn = greenBtn("Accedi");
        Button backBtn  = blackBtn("← Indietro");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene()));

        loginBtn.setOnAction(e -> {
            hideErr(err);
            String user = usernameField.getText().trim();
            String psw = passwordField.getText();
            if (user.isEmpty() || psw.isEmpty()) { showError(err, "Tutti i campi devono essere completati."); return; }
            try {
                Message res = client.send(new Message("login", new Object[]{user, psw}));
                if (res.getOp().equals("OK")) {                
                } else {
                    showError(err, "Credenziali errate. Riprova.");
                    passwordField.clear();
                }
            } catch (ClassNotFoundException | IOException ioc) {
                showError(err, "Errore durante la connessione con il server...");
            }
        });

        Label noAcc = new Label("Non hai un account?");
        noAcc.setStyle("-fx-font-size:12;");
        Hyperlink regText = new Hyperlink("Registrati");
        regText.setOnAction(e -> stage.setScene(registerScene()));
        HBox reg = new HBox(4, noAcc, regText);
        reg.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, usernameField, passwordField, err, loginBtn, backBtn, reg);
        return makeScene(layout, WIDTH, HEIGHT);
    }

    private Scene registerScene() {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        Label title = new Label("Registrati");
        title.setStyle("-fx-font-size:22; -fx-font-weight:bold;");

        TextField nomeField = field("Nome");
        TextField cognomeField = field("Cognome");
        TextField usernameField = field("Username");
        TextField domicilioField = field("Domicilio (esempio: Viale Luigi Cadorna 10, Busto Arsizio, Italia");
        PasswordField pswField = passField("Password");
        PasswordField pswConfField = passField("Conferma password");

        ToggleGroup ruoloBtns = new ToggleGroup();
        RadioButton ruolo_cliente = new RadioButton("Cliente");
        RadioButton ruolo_ristoratore = new RadioButton("Ristoratore");
        ruolo_cliente.setToggleGroup(ruoloBtns);
        ruolo_ristoratore.setToggleGroup(ruoloBtns);
        ruolo_cliente.setSelected(true);
        HBox ruoloRow = new HBox(20, ruolo_cliente, ruolo_ristoratore);
        ruoloRow.setAlignment(Pos.CENTER);

        Label err = errorLabel();

        Button regBtn  = greenBtn("Crea account");
        Button backBtn = blackBtn("Indietro");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene()));

        regBtn.setOnAction(e -> {
            hideErr(err);
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String username = usernameField.getText().trim();
            String domicilio = domicilioField.getText().trim();
            String psw = pswField.getText();
            String pswConf = pswConfField.getText();
            String ruolo = ruolo_cliente.isSelected() ? "utente" : "ristoratore";

            if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || domicilio.isEmpty() || psw.isEmpty() || pswConf.isEmpty()) {
                showError(err, "Tutti i campi devono essere completati."); return;
            }
            if (!psw.equals(pswConf)) { showError(err, "Le password non coincidono..."); return; }

            try {
                Message res = client.send(new Message("register", new Object[]{nome, cognome, username, Password.encrypt(psw), null, domicilio, ruolo}));
                if (res.getOp().equals("OK")) {
                    stage.setScene(loginScene());
                } else {
                    showError(err, "Username già esistente.");
                }
            } catch (ClassNotFoundException | IOException ex) {
                showError(err, "Errore di connessione al server.");
            }
        });

        layout.getChildren().addAll( title, nomeField, cognomeField, usernameField, domicilioField, pswField, pswConfField, ruoloRow, err, regBtn, backBtn);

        return makeScene(layout, WIDTH, HEIGHT);
    }
}