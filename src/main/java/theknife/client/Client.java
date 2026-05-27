/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */

package theknife.client;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import theknife.client.gui.Welcome;

/**
 * Classe Client.
 * Estende {@link Application} e gestisce l'applicazione JavaFX lato client. 
 */
public class Client extends Application {

    /**
     * Inizializza l'applicazione JAVAFX. Imposta le dimensioni default,
     * logo etc..
     * @param stage {@link Stage}
     */
    @Override
    public void start(Stage stage) {
        ClientManager client = new ClientManager();
        
        stage.setWidth(theknife.client.gui.GUIBasics.WIDTH);
        stage.setHeight(theknife.client.gui.GUIBasics.HEIGHT);
        stage.setMinWidth(theknife.client.gui.GUIBasics.MIN_WIDTH); 
        stage.setMinHeight(theknife.client.gui.GUIBasics.MIN_HEIGHT);

        Image logo = new Image(getClass().getResourceAsStream("/logo_app.png"));
        stage.getIcons().add(logo);    

        new Welcome(stage, client).show();
    }

    /**
     * Metodo main.
    */
    public static void main(String[] args) {
        launch(args);
    }
}