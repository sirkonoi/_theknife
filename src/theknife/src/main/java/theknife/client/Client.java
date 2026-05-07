package theknife.client;

import javafx.application.Application;
import javafx.stage.Stage;
import theknife.client.gui.Welcome;

public class Client extends Application {

    @Override
    public void start(Stage stage) {
        ClientManager client = new ClientManager();
        new Welcome(stage, client).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}