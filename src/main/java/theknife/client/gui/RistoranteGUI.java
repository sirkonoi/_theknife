/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */

package theknife.client.gui;

import java.io.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import theknife.*;
import theknife.client.ClientManager;
import theknife.server.models.*;

/**
 * Classe RistoranteGUI.
 * Gestisce la visualizzazione di uno specifico ristorante selezionato.
 * Possibilità di aggiungere recensioni, risposte.
 */
public class RistoranteGUI implements GUIBasics {

    /**
     * Lo stage dell'applicazione.
     */
    private Stage stage;

    /**
     * Il gestore connessione.
     */
    private ClientManager client;

/**
     * Utente loggato (Utente o Ristoratore).
     */    
    private Utente utente;

    /**
     * Utente non registrato (Ospite).
     */
    private Guest guest;

    /**
     * Indica se l'accesso è in modalità guest.
     */    
    private boolean guestHome;

    /**
     * Il ristorante da visualizzare.
     */    
    private Ristorante ristorante;

    /**
     * Scene precedente.
     */    
    private Scene previousScene;


    /**
     * Costruttore per utente loggato.
     *
     * @param stage
     * @param client        {@link ClientManager} gestore connessione.
     * @param utente        {@link Utente} loggato.
     * @param ristorante    {@link Ristorante} da visualizzare.
     * @param previousScene {@link Scene} per tornare indietro.
     */    
    public RistoranteGUI(Stage stage, ClientManager client, Utente utente, Ristorante ristorante, Scene previousScene) {
        this.stage = stage;
        this.client = client;
        this.utente = utente;
        this.ristorante = ristorante;
        this.previousScene = previousScene;
        this.guestHome = false;
    }

        /**
     * Costruttore per guest.
     *
     * @param stage
     * @param client        {@link ClientManager} gestore connessione.
     * @param guest        {@link Guest} ospite.
     * @param ristorante    {@link Ristorante} da visualizzare.
     * @param previousScene {@link Scene} per tornare indietro.
     */ 
    public RistoranteGUI(Stage stage, ClientManager client, Guest guest, Ristorante ristorante, Scene previousScene) {
        this.stage = stage;
        this.client = client;
        this.guest = guest;
        this.ristorante = ristorante;
        this.previousScene = previousScene;
        this.guestHome = true;
    }

    /**
     * Imposta edsullo stage principale la scena del ristorante.
     */
    public void show() {
        stage.setScene(restaurantScene());
    }

    /**
     * Crea la scena di visualizzazione del ristorante,
     *
     * @return {@link Scene} del dato ristorante. 
     */
    private Scene restaurantScene() {
        HBox root = new HBox(20);
        root.setStyle("-fx-background-color: #1a1a1a;");
        root.setPadding(new Insets(24));

        VBox leftCol = new VBox(16);
        leftCol.setPrefWidth(320);
        leftCol.setMinWidth(260);

        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        Label nomeLabel = new Label(ristorante.getNome());
        nomeLabel.getStyleClass().add("ristorante-titolo");
        nomeLabel.setWrapText(true);

        leftCol.getChildren().addAll(backBtn, nomeLabel, GUIComponents.infoRistoranteBox(ristorante));

        boolean isOwner = !guestHome && utente.getRuolo().equals("ristoratore") && utente.getId() == ristorante.getRistoratore();

        if (!guestHome && !isOwner) {
            Button prefBtn = GUIComponents.blackBtn("❤ Aggiungi ai preferiti");
            prefBtn.setMaxWidth(Double.MAX_VALUE);
            prefBtn.setOnAction(e -> {
                try {
                    client.send(new Message("addPreferiti", new Object[] { utente.getId(), ristorante.getId(), ristorante.getNome() }));
                    GUIComponents.alert(Alert.AlertType.INFORMATION, "Preferiti", "Ristorante aggiunto!");                
                
                } catch (ClassNotFoundException | IOException ex) {
                    GUIComponents.alert(Alert.AlertType.ERROR, "Errore", "Impossibile salvare il preferito.");
                    System.out.println("Errore nell'aggiungere il preferito.");
                }
            });
            leftCol.getChildren().add(prefBtn);
        }

        if (!guestHome && !isOwner) {
            leftCol.getChildren().add(GUIComponents.formRecensione(ristorante, utente, client, this::show));
        }

        VBox rightCol = new VBox(12);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        Label recTitle = new Label("Recensioni");
        recTitle.getStyleClass().add("sezione-titolo");

        VBox boxRecensioni = new VBox(10);
        caricaRecensioni(boxRecensioni, isOwner);

        ScrollPane scroll = GUIComponents.scrollPane(boxRecensioni);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        rightCol.getChildren().addAll(recTitle, scroll);
        root.getChildren().addAll(leftCol, rightCol);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;
    }

    /**
     * Carica la lista di recensioni e le aggiunge al box di recensioni.
     *
     * @param boxRecensioni {@link VBox} che conterrà le recensioni
     * @param isOwner Indica se l'utente e' owner del ristorante.
     */
    private void caricaRecensioni(VBox boxRecensioni, boolean isOwner) {
        boxRecensioni.getChildren().clear();
        try {
            Message res = client.send(new Message("recensioniRistorante", new Object[] { ristorante.getId() }));
            List<Recensione> recensioni = (List<Recensione>) res.getDati()[0];

            if (recensioni == null || recensioni.isEmpty()) {
                Label noRec = new Label("Nessuna recensione disponibile.");
                noRec.getStyleClass().add("preferiti-empty");
                boxRecensioni.getChildren().add(noRec);
                return;
            }

            for (Recensione r : recensioni) {
                boxRecensioni.getChildren().add(GUIComponents.recensioneBox(r, isOwner, guestHome, utente, client, () -> caricaRecensioni(boxRecensioni, isOwner)));
            }
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Errore nel caricare le recensioni.");
        }
    }
}