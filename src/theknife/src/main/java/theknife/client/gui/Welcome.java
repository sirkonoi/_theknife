package theknife.client.gui;

import java.io.IOException;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import theknife.client.ClientManager;
import theknife.client.Message;

public class Welcome {

    private Stage stage;
    private ClientManager client;

    public Welcome(Stage stage, ClientManager client) {
        this.stage = stage;
        this.client = client;
    }

    public void show() {
        stage.setScene(welcome());
        stage.setTitle("TheKnife - Benvenuto");
        stage.show();
    }

    private Scene welcome() {
        Button userLog = new Button("Accedi come Utente");
        Button guestLog = new Button("Accedi come Guest");

        userLog.setOnAction(e -> stage.setScene(loginScene()));
        //guestLog.setOnAction(e -> stage.setScene(guestScene()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(new Label("Benvenuto su TheKnife"), userLog, guestLog);
        layout.setAlignment(Pos.CENTER);
        return new Scene(layout, 700, 400);
    }
    private Scene loginScene() {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Indietro");

        backBtn.setOnAction(e -> stage.setScene(welcome()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(new Label("Login"), usernameField, passwordField, loginBtn, backBtn);
        layout.setAlignment(Pos.CENTER);
        loginBtn.setOnAction(e -> {
            Message request = new Message("login", new Object[]{usernameField.getText(), passwordField.getText()});
            try {
                Message response = client.send(request);
                if(response.getOp().equals("OK")) {
                    Label test = new Label("OK LOGGATO");
                    layout.getChildren().add(test);
                }
                if(response.getOp().equals("ERROR")) {
                    Label test = new Label("NON OK: NON LOGGATO");
                    layout.getChildren().add(test);
                }                
                
            } catch (ClassNotFoundException | IOException e1) {}
        });        
        return new Scene(layout, 700, 400);
    }    
}