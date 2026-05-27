/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */

package theknife.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import theknife.Message;
import theknife.client.ClientManager;
import theknife.server.models.Guest;
import theknife.server.models.Recensione;
import theknife.server.models.Ristorante;
import theknife.server.models.Utente;

/**
 * Classe GUIComponents.
 * Fornisce metodi per la creazione, la formattazione e la configurazione
 * degli elementi grafici e layout dell'interfaccia client.
 */
public class GUIComponents implements GUIBasics {

    /**
     * Crea un textField formattato con testo fornito e larghezza fissa.
     *
     * @param text Il testo da mostrare nel campo.
     * @return {@link TextField} formattato.
     */
    public static TextField field(String text) {
        TextField f = new TextField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        f.setMaxWidth(FIELD_WIDTH);
        return f;
    }

    /**
     * Crea un PasswordField per password formattato con testo fornito e larghezza fissa.
     *
     * @param text Il testo da mostrare nel campo.
     * @return {@link PasswordField} formattato.
     */
    public static PasswordField passField(String text) {
        PasswordField f = new PasswordField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        f.setMaxWidth(FIELD_WIDTH);
        return f;
    }

    /**
     * Genera un pulsante verde formattato.
     *
     * @param text Il testo da visualizzare nel pulsante.
     * @return {@link Button} formattato.
     */    
    public static Button greenBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(BTN_WIDTH);
        b.getStyleClass().add("btn-green");
        return b;
    }

    /**
     * Genera un pulsante nero formattato.
     *
     * @param text Il testo da visualizzare nel pulsante.
     * @return {@link Button} formattato.
     */ 
    public static Button blackBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(BTN_WIDTH);
        b.getStyleClass().add("btn-black");
        return b;
    }

    /**
     * Genera un pulsante di logout per uscire dall'app.
     *
     * @return {@link Button} formattato e che fa terminare l'applicazione se premuto.
     */ 
    public static Button logoutButton() {
        Button btn = new Button("⛔ Esci");
        btn.getStyleClass().add("logout-btn");
        btn.setOnAction(e -> System.exit(0));
        return btn;
    }   

    /**
     * Crea un bottone specifico per la sidebar della homepage.
     *
     * @param testo Il testo da visualizzare nel pulsante.
     * @return {@link Button} formattato.
     */
    public static Button sidebarBtn(String testo) {
        Button b = new Button(testo);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.getStyleClass().add("sidebar-btn");
        return b;
    }  

    /**
     * Inizializza un'etichetta preposta alla segnalazione degli errori,
     * inizialmente è nascosta.
     *
     * @return {@link Label} per i messaggi d'errore.
     */
    public static Label errorLabel() {
        Label l = new Label();
        l.getStyleClass().add("error-label");
        l.setVisible(false);
        l.setManaged(false);
        return l;
    }

    /**
     * Mostra l'etichetta di errore specificata.
     *
     * @param l L'etichetta {@link Label} da abilitare.
     * @param msg Il messaggio dell'errore da mostrare.
     */
    public static void showError(Label l, String msg) {
        l.setText("Errore: " + msg);
        l.setVisible(true);
        l.setManaged(true);
    }

    /**
     * Nasconde un'etichetta d'errore.
     *
     * @param l L'etichetta {@link Label} da nascondere.
     */
    public static void hideError(Label l) {
        l.setVisible(false);
        l.setManaged(false);
    }

    /**
     * Genera un contenitore che contiene il logo di The Knife.
     *
     * @return Un {@link VBox} contentente il logo.
     */
    public static VBox logo() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        try {
            ImageView img = new ImageView(new Image(GUIComponents.class.getResourceAsStream("/logo.png")));
            img.setFitHeight(70);
            img.setPreserveRatio(true);
            box.getChildren().add(img);
        } catch (Exception e) {}
        return box;
    }
    
    /**
     * Genera un contenitore che contiene il logo (versione piccola) di The Knife.
     *
     * @return Un {@link VBox} contentente il logo.
     */
    public static VBox miniLogo() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        try {
            ImageView img = new ImageView(new Image(GUIComponents.class.getResourceAsStream("/logo.png")));
            img.setFitHeight(30);
            img.setPreserveRatio(true);
            box.getChildren().add(img);
        } catch (Exception e) {}
        return box;
    }  
    
    /**
     * Crea e formatta la VBox per la navigazione nell'homepage.
     *
     * @return {@link VBox} formattato.
     */
    public static VBox sidebar() {
        VBox sidebar = new VBox(12);
        sidebar.getStyleClass().add("side-bar");
        return sidebar;
    }

    /**
     * Crea un label formattato per la visualizzazione dell'username nella sidebar.
     *
     * @param username La stringa relativa al nome utente.
     * @return {@link Label} formattato.
     */
    public static Label username(String username) {
        Label label = new Label(username);
        label.getStyleClass().add("username");
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    } 
    
    /**
     * Crea un separatore.
     *
     * @return {@link Separator}.
     */
    public static Separator separator() {
        Separator sep = new Separator();
        sep.getStyleClass().add("separator");
        return sep;
    }
    
    /**
     * Crea una regione vuota, usata per occupare tutto lo spazio disponibili in contenitori (es. per la sidebar).
     *
     * @return {@link Region} formattata.
     */
    public static Region spacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }   
    
    /**
     * Crea un menu' a tendina contenente l'elenco delle tipologie di cucina disponibili per i filtri.
     *
     * @param lista La lista di tipi dei ristoranti.
     * @return {@link ComboBox} formattato.
     */
    public static ComboBox<String> tipiCucinaBox(List<String> lista) {
        ComboBox<String> tipi = new ComboBox<>();
        tipi.setPromptText("Cucina");
        tipi.setPrefWidth(110);
        tipi.getItems().add("Nessuna");
        tipi.getItems().setAll(lista);
        tipi.getStyleClass().add("combobox");
        return tipi;
    }  
    
    /**
     * Crea un menu' a tendina contenente l'elenco dei prezzi medi per i filtri.
     *
     * @return {@link ComboBox} formattato.
     */
    public static ComboBox<String> prezzoBox() {
        ComboBox<String> prezzo = new ComboBox<>();
        prezzo.setPromptText("Prezzo");
        prezzo.setPrefWidth(100);
        prezzo.getItems().addAll("< 20€", "20-50€", "50-100€", "> 100€");
        prezzo.getStyleClass().add("combobox");
        return prezzo;
    }  
    
    /**
     * Crea una checkbox da usare per filtri booleani (es. delivery, booking..)
     *
     * @param testo Testo associato alla checkbox
     * @return {@link CheckBox} formattata.
     */
    public static CheckBox filterCheckBox(String testo) {
        CheckBox check = new CheckBox(testo);
        check.getStyleClass().add("checkbox");
        return check;
    }  
    
    /**
     * Aggiunge la scrollbar a un nodo, inoltre disabilita lo scorrimento orizzontale.
     *
     * @param content Il nodo {@link Node} da rendere scorrevole.
     * @return {@link ScrollPane} formattato.
     */
    public static ScrollPane scrollPane(Node content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("scroll");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return scroll;
    }

    /**
     * Fabbrica una card per rappresentare un singolo ristorante, con dati,
     * pulsanti di navigazione (visualizzare i dettagli) e per l'inserimento nei preferiti.
     *
     * @param ristorante   Oggetto {@link Ristorante} da visualizzare.
     * @param media        Media numero stelle.
     * @param numRec       Numero totale di recensioni.
     * @param utente       Il profilo {@link Utente} se loggato.
     * @param guest        Il profilo ospite {@link Guest} qualora l'accesso sia senza login.
     * @param guestHome    Variabile booleana che indica se l'accesso è di tipo guest.
     * @param stage        {@link Stage} usato per lo switch di scena.
     * @param client       {@link ClientManager} gestore della connessione.
     * @param currentScene La scena corrente per eventuali "indietro", ovvero tornare alla scena precedente.
     * @return {@link HBox} formattato.
     */
    public static HBox ristoranteCard(Ristorante ristorante, double media, int numRec, Utente utente, Guest guest, boolean guestHome, Stage stage, ClientManager client, Scene currentScene) {        
        HBox card = new HBox(16);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("ristorante-card");

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nomeLabel = new Label(ristorante.getNome());
        nomeLabel.getStyleClass().add("card-titolo");
        List<String> tipi = new ArrayList<>();
        try {
            Message res = client.send(new Message("getTipoRistorante", new Object[]{ristorante.getId()}));
            tipi = (List<String>) res.getDati()[0];
        } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei tipi...");}        
       
        String listaTipi = String.join(", ", tipi); 

        if (listaTipi.isEmpty()) {
            listaTipi = "Nessuna cucina specificata";
        }

        Label dettagliLabel = new Label(ristorante.getLuogo().getIndirizzo() + " - " + listaTipi + " - " + ristorante.getFasciaPrezzo() + "€");
        dettagliLabel.getStyleClass().add("card-sottotitolo");
        Label stelleLabel = new Label("⭐ " + (media == 0.0 ? 0 : media) + " (" + numRec + (numRec == 1 ? " recensione)" : " recensioni)"));
        stelleLabel.getStyleClass().add("card-stelle");

        info.getChildren().addAll(nomeLabel, dettagliLabel, stelleLabel);

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button dettagliBtn = GUIComponents.blackBtn("Dettagli");
        actions.getChildren().add(dettagliBtn);
        dettagliBtn.setOnAction(e -> {
            Scene scene = stage.getScene();
            if (guestHome) {
                new RistoranteGUI(stage, client, guest, ristorante, scene).show();
            } else {
                new RistoranteGUI(stage, client, utente, ristorante, scene).show();
            } 
        });

        if (!guestHome) {
            Button prefBtn = GUIComponents.blackBtn("❤");
            actions.getChildren().add(prefBtn);
            prefBtn.setOnAction(e -> {
            try {
                Message res = client.send(new Message("addPreferiti", new Object[]{utente.getId(), ristorante.getId(), ristorante.getNome()}));
                alert(Alert.AlertType.INFORMATION, "The Knife", "Ristorante aggiunto!");                
            } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nell'aggiungere il preferito..."); alert(Alert.AlertType.ERROR, "Errore", "Impossibile salvare il preferito.");}               
            });
        }

        card.getChildren().addAll(info, actions);
        return card;
    }

    /**
     * Alert utilizzato per notifiche, errori etc..
     *
     * @param tipo    Tipo di alert {@link Alert.AlertType}.
     * @param titolo  Titolo dell'alert.
     * @param testo   Messaggio di informazione, errore etc...
     * @return {@link Alert} da mostrare.
     */
    public static Alert alert(Alert.AlertType tipo, String titolo,  String testo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(testo);
        alert.showAndWait();
        return alert;
    }

    /**
     * Genera un box con slider per la distanza geografica.
     *
     * @param sliderRef riferimento allo slider.
     * @return {@link VBox} contenente lo slider per la scelta della distanza.
     */
    public static VBox sliderDistanza(Slider[] sliderRef) {
        Slider slider = new Slider(5, 200, 30);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(50);
        slider.setBlockIncrement(5);
        slider.setPrefWidth(120);

        Label distanzaLabel = new Label("30 km");
        distanzaLabel.getStyleClass().add("filter-label");
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
            distanzaLabel.setText((int) newVal.doubleValue() + " km")
        );

        Label title = new Label("Raggio:");
        title.getStyleClass().add("filter-title");

        sliderRef[0] = slider;
        return new VBox(2, title, slider, distanzaLabel);
    }  
    
    /**
     * Crea un avatar contenente la lettera iniziale dell'username utente.
     *
     * @param nome username utente.
     * @return {@link StackPane}.
     */
    public static StackPane avatar(String nome) {
        Circle cerchio = new Circle(26);
        cerchio.setStyle("-fx-fill: #2a2a2a; -fx-stroke: #4caf50; -fx-stroke-width: 2;");
        String iniziale = (nome != null && !nome.isEmpty()) ? String.valueOf(nome.charAt(0)).toUpperCase() : "?";
        Label inizialeLabel = new Label(iniziale);
        inizialeLabel.getStyleClass().add("iniziale-label");
        StackPane box = new StackPane(cerchio, inizialeLabel);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Crea il box del profilo utente contenente l'avatar, il nome, cognome,
     * username, domicilio e ruolo.
     *
     * @param nome      Nome, cognome.
     * @param ruolo     Ruolo.
     * @param username  Username.
     * @param domicilio Domicilio dell'utente.
     * @return {@link VBox}, profilo utente. 
     */
    public static VBox profiloCard(String nome, String ruolo, String username, String domicilio) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(28, 36, 28, 36));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(400);
        card.getStyleClass().add("profilo-card");

        StackPane avatarBox = avatar(nome);

        Label nomeLabel = new Label(nome);
        nomeLabel.getStyleClass().add("nome-label");
        Label ruoloLabel = new Label(ruolo);
        ruoloLabel.getStyleClass().add("ruolo-label");
        VBox nameBox = new VBox(4, nomeLabel, ruoloLabel);
        nameBox.setAlignment(Pos.CENTER);

        Separator sep = separator();
        VBox.setMargin(sep, new Insets(4, 0, 4, 0));

        card.getChildren().addAll(avatarBox, nameBox, sep,infoRow("Username", username), infoRow("Domicilio", domicilio));
        return card;
    }

    /**
     * Crea una riga formattata contenente le informazioni passate.
     *
     * @param titolo Titolo del campo.
     * @param testo  Valore associato al campo.
     * @return {@link HBox} 
     */
    public static HBox infoRow(String titolo, String testo) {
        Label titoloLabel = new Label(titolo + ": ");
        titoloLabel.getStyleClass().add("info-titolo");
        Label testoLabel = new Label(testo);
        testoLabel.getStyleClass().add("info-valore");
        HBox row = new HBox(8, titoloLabel, testoLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }   
    
    /**
     * Crea il VBox della sezione preferiti, contenente la lista dei preferiti dell'utente.
     *
     * @param lista        Lista di {@link Ristorante} preferiti.
     * @param utente       {@link Utente} che sta visualizzando i preferiti. 
     * @param stage
     * @param client       {@link ClientManager} gestore connessione.
     * @param currentScene Scena per "indietro".
     * @param rimuovi      {@link Runnable} per eseguire refresh della pagina quando si rimuovono preferiti etc...
     * @return {@link VBox} formattato per visualizzare i preferiti dell'utente.
     */
    public static VBox preferiti(List<Ristorante> lista, Utente utente, Stage stage, ClientManager client, Scene currentScene, Runnable rimuovi) {
        VBox box = new VBox(16);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: #1a1a1a;");

        Label titolo = new Label("❤  I miei preferiti");
        titolo.getStyleClass().add("preferiti-titolo");

        VBox boxRistoranti = new VBox(10);

        if (lista == null || lista.isEmpty()) {
            Label empty = new Label("Nessun ristorante nei preferiti.");
            empty.getStyleClass().add("preferiti-empty");
            boxRistoranti.getChildren().add(empty);
        } else {
            for (Ristorante ris : lista) {
                HBox card = card(ris, utente, stage, client, currentScene, rimuovi, true, false);
                boxRistoranti.getChildren().add(card);
            }
        }

        ScrollPane scroll = scrollPane(boxRistoranti);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        box.getChildren().addAll(titolo, scroll);
        return box;
    }

    /**
     * Crea una card ristorante per la visualizzazione in liste di gestione
     * (es. preferiti, miei ristoranti), modificandola dinamicamente in base ai parametri.
     *
     * @param r            Il {@link Ristorante} da visualizzare.
     * @param utente       L'utente {@link Utente}.
     * @param stage
     * @param client       {@link ClientManager} gestore connessione.
     * @param currentScene La scena per eventuali bottoni "indietro".
     * @param rimuovi      {@link Runnable} per il refresh della scena.
     * @param enableBtns   Boolean per includere il bottone di cancellazione dalla lista preferiti.
     * @param enableStats  Boolean per abilitare il caricamento delle statistiche (media stelle, numero recensioni) del ristorante.
     * @return {@link HBox} formattato.
     */
    public static HBox card(Ristorante r, Utente utente, Stage stage, ClientManager client, Scene currentScene, Runnable rimuovi, boolean enableBtns, boolean enableStats) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("ristorante-card");

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nomeLabel = new Label(r.getNome());
        nomeLabel.getStyleClass().add("card-titolo");
        
        String citta = (r.getLuogo().getIndirizzo() != null) ? r.getLuogo().getIndirizzo() : "Nessun indirizzo trovato.";
        Label dettagliLabel = new Label(citta + " · " + r.getFasciaPrezzo() + "€");
        dettagliLabel.getStyleClass().add("card-sottotitolo");

        if(enableStats) {
            Label stats = new Label();
            try {
                Message res = client.send(new Message("infoRecensioni", new Object[]{r.getId()}));
                double[] infoRec = (double[]) res.getDati()[0];
                stats.setText("⭐ " + infoRec[0] + " | " + (int) infoRec[1] + " recensioni");
            }catch(ClassNotFoundException | IOException exc) {
                System.out.println("Errore nella rimozione del preferito.");
                alert(Alert.AlertType.ERROR, "Errore", "Impossibile trovare stats ristorante.");             
            }
            info.getChildren().addAll(nomeLabel, dettagliLabel, stats);
        }

        else { info.getChildren().addAll(nomeLabel, dettagliLabel); }

        

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button dettagliBtn = blackBtn("Dettagli");
        dettagliBtn.setOnAction(e -> new RistoranteGUI(stage, client, utente, r, currentScene).show());

        if(enableBtns) {
            Button rimuoviBtn = blackBtn("Rimuovi");
            rimuoviBtn.setOnAction(e -> {
                try {
                    client.send(new Message("removePreferiti", new Object[]{utente.getId(), r.getId()}));
                    alert(Alert.AlertType.INFORMATION, "The Knife", "Ristorante rimosso dai preferiti.");
                    rimuovi.run();
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nella rimozione del preferito.");
                    alert(Alert.AlertType.ERROR, "Errore", "Impossibile rimuovere il ristorante.");
                }
        });
            actions.getChildren().addAll(dettagliBtn, rimuoviBtn);

        }

        else if(enableStats) {
            Button deleteBtn = greenBtn("Elimina");
            deleteBtn.setOnAction(e ->{
                try {
                     System.out.println("DEBUG CLIENT: Cliccato elimina su ristorante: " + r.getNome() + " con ID: " + r.getId());                    
                    Message res = client.send(new Message("deleteRistorante", new Object[]{r.getId()}));
                    alert(Alert.AlertType.INFORMATION, "The Knife", "Ristorante eliminato con successo.");
                    rimuovi.run();                    
                }catch(ClassNotFoundException | IOException exce) {
                    System.out.println("Errore nella rimozione del ristorante.");
                    alert(Alert.AlertType.ERROR, "Errore", "Impossibile rimuovere il ristorante."); 
                }
            });
            actions.getChildren().addAll(dettagliBtn, deleteBtn);             
        }
        else {actions.getChildren().addAll(dettagliBtn);}

        card.getChildren().addAll(info, actions);
        return card;
    }   

    /**
     * VBox contenente le informazioni di un ristorante come indirizzo, prezzo medio etc..
     *
     * @param ristorante {@link Ristorante} da utilizzare.
     * @return {@link VBox} formattato per la visualizzazione dei dettagli.
     */
    public static VBox infoRistoranteBox(Ristorante ristorante) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.getStyleClass().add("info-card");
        card.getChildren().addAll(infoRow("Indirizzo", ristorante.getLuogo().getIndirizzo()), infoRow("Prezzo medio", ristorante.getFasciaPrezzo() + "€"), infoRow("Delivery", ristorante.isDelivery() ? "✅ Effettuato" : "❌ Non effettuato"), infoRow("Prenotazione", ristorante.isPrenotazioneOnline() ? "✅ Disponibile" : "❌ Non disponibile"));
        return card;
    }

    /**
     * Crea il box dedicato alle recensioni da usare nella visualizzazione dei dettagli di un ristorante.
     * Abilita o disabilita dinamicamente pulsanti etc... sulla base dei parametri passati.
     *
     * @param rec       {@link Recensione}.
     * @param isOwner   Indica se l'utente loggato e' il proprietario o meno del ristorante.
     * @param guestHome Indica se l'accesso e' stato effettuato come guest.
     * @param utente    {@link Utente} loggato.
     * @param client    {@link ClientManager} gestore connessione.
     * @param ricarica  {@link Runnable} per il refresh.
     * @return {@link VBox} formattata.
     * @throws ClassNotFoundException Fallisce la deserializzazione.
     * @throws IOException            Errore I/O.
     */
    public static VBox recensioneBox(Recensione rec, boolean isOwner, boolean guestHome, Utente utente, ClientManager client, Runnable ricarica) throws ClassNotFoundException, IOException {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.getStyleClass().add("recensione-box");

        Utente recensore = null;
        
        Message res = client.send(new Message("getUtenteFromId", new Object[]{rec.getIdRecensore()}));
        recensore = (Utente) res.getDati()[0];

        Label autoreLabel = recensore == null ? new Label("Anonimo") : new Label("Utente: " + recensore.getUsername()) ;
        autoreLabel.getStyleClass().add("recensione-autore");
        Label stelleLabel = new Label("⭐" + rec.getStelle() + " stelle");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(10, autoreLabel, spacer, stelleLabel);
        top.setAlignment(Pos.CENTER_LEFT);

        Label testoLabel = new Label(rec.getTesto());
        testoLabel.getStyleClass().add("recensione-testo");
        testoLabel.setWrapText(true);

        card.getChildren().addAll(top, testoLabel);

        if (rec.getRisposta() != null) {
            VBox rispostaBox = new VBox(4);
            rispostaBox.setPadding(new Insets(8, 12, 8, 12));
            rispostaBox.getStyleClass().add("risposta-box");
            Label rispostaTitle = new Label("Risposta del ristoratore:");
            rispostaTitle.getStyleClass().add("risposta-titolo");
            Label rispostaLabel = new Label(rec.getRisposta());
            rispostaLabel.getStyleClass().add("risposta-testo");
            rispostaLabel.setWrapText(true);
            rispostaBox.getChildren().addAll(rispostaTitle, rispostaLabel);
            card.getChildren().add(rispostaBox);
        }

        if (isOwner && rec.getRisposta() == null) {
            TextField rispostaField = field("Scrivi una risposta...");
            rispostaField.setMaxWidth(Double.MAX_VALUE);
            Button rispondiBtn = greenBtn("Rispondi");
            rispondiBtn.setOnAction(e -> {
                String testo = rispostaField.getText().trim();
                if (testo.isEmpty()) return;
                try {
                    client.send(new Message("rispondiRecensione", new Object[]{rec.getId(), testo}));
                    alert(AlertType.INFORMATION, "The Knife", "Risposta aggiunta con successo.");
                    ricarica.run();
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nell'inviare la risposta.");
                    alert(AlertType.ERROR, "Errore", "Impossibile aggiungere la risposta. Riprova.");
                }
            });
            card.getChildren().addAll(rispostaField, rispondiBtn);
        }

        if(isOwner && rec.getRisposta() != null) {
            Button eliminaBtn = greenBtn("Elimina risposta");
            eliminaBtn.setOnAction(e -> {
                try {
                    client.send(new Message("eliminaRisposta", new Object[]{rec.getId()}));
                    alert(AlertType.INFORMATION, "The Knife", "Risposta eliminata con successo.");
                    ricarica.run();
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nell'eliminare la risposta.");
                    alert(AlertType.ERROR, "Errore", "Impossibile eliminare la risposta. Riprova.");

                }                
            });            
            card.getChildren().addAll(eliminaBtn);
        }

        if (!guestHome && utente.getId() == rec.getIdRecensore()) {
            Button eliminaBtn = blackBtn("Elimina");
            eliminaBtn.setOnAction(e -> {
                try {
                    client.send(new Message("removeRecensione", new Object[]{rec.getId()}));
                    ricarica.run();
                    alert(AlertType.INFORMATION, "The Knife", "Recensione eliminata con successo.");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nell'eliminare la recensione.");
                    alert(AlertType.ERROR, "Recensione", "Impossibile eliminare la recensione. Riprova.");
                }
            });
            TextField modificaField = field("Nuovo testo recensione...");
            modificaField.setVisible(false);
            modificaField.setManaged(false);  
            
            Button modificaBtn = blackBtn("Modifica");
            modificaBtn.setOnAction(e -> {
                if (!modificaField.isVisible()) {
                    modificaField.setVisible(true);
                    modificaField.setManaged(true);
                    modificaField.setText(rec.getTesto());
                    modificaBtn.setText("Conferma");
                } else {
                    String nuovoTesto = modificaField.getText().trim();
                    if (nuovoTesto.isEmpty()) return;
                    try {
                        client.send(new Message("modifyRecensione", new Object[]{rec.getId(), nuovoTesto}));
                        alert(AlertType.INFORMATION, "The Knife", "Recensione modificata con successo.");
                        ricarica.run();
                    } catch (ClassNotFoundException | IOException ex) {
                        alert(AlertType.ERROR, "Recensione", "Impossibile modificare la recensione.");
                    }
                }
            });            
            
            card.getChildren().addAll(eliminaBtn, modificaField, modificaBtn);
        }
        return card;
    }

    /**
     * Form per aggiunta di recensioni per un dato ristorante.
     * 
     * @param ristorante {@link Ristorante} che si sta recensendo.
     * @param utente     {@link Utente} che sta recensendo il ristorante.
     * @param client     {@link ClientManager} gestore connessione.
     * @param refresh      {@link Runnable} per il refresh (dopo inserimento recensione).
     * @return {@link VBox} il form per la recensione.
     */
    public static VBox formRecensione(Ristorante ristorante, Utente utente, ClientManager client, Runnable refresh) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("form-recensione");

        Label formTitle = new Label("Scrivi una recensione");
        formTitle.getStyleClass().add("form-titolo");

        ComboBox<Integer> stelle = new ComboBox<>();
        stelle.getItems().addAll(1,2,3,4,5);
        stelle.setPromptText("⭐ Stelle");
        stelle.setMaxWidth(Double.MAX_VALUE);

        TextField testoField = field("Come ti sei trovato..?");
        testoField.setMaxWidth(Double.MAX_VALUE);

        Label err = errorLabel();

        Button inviaBtn = greenBtn("Invia la recensione");
        inviaBtn.setMaxWidth(Double.MAX_VALUE);
        inviaBtn.setOnAction(e -> {
            if (stelle.getValue() == null || testoField.getText().trim().isEmpty()) {
                showError(err, "Compila tutti i campi.");
                return;
            }
            try {
                Message res = client.send(new Message("addRecensione", new Object[]{new Recensione(0, utente.getId(), ristorante.getId(), testoField.getText().trim(), stelle.getValue(), new java.util.Date(), null)}));                   
                if (res.getOp().equals("OK")) {
                    stelle.setValue(null);
                    testoField.clear();
                    hideError(err);
                    refresh.run();
                    alert(AlertType.INFORMATION, "The Knife", "Recensione aggiunta con successo.");
                } else {
                    showError(err, "Hai già recensito questo ristorante.");
                    alert(AlertType.ERROR, "Recensione", "Aggiunta recensione fallita... Hai gia' scritto una recensione per questo ristorante!");
                }
            } catch (ClassNotFoundException | IOException ex) {
                showError(err, "Errore nell'inviare la recensione.");
                alert(AlertType.ERROR, "Recensione", "Aggiunta recensione fallita...");

            }
        });

        form.getChildren().addAll(formTitle, stelle, testoField, err, inviaBtn);
        return form;
    }   
    
    /**
     * Vbox per la sezione "I miei ristoranti"
     *
     * @param lista        La lista di {@link Ristorante} del proprietario.
     * @param utente       {@link Utente} proprietario.
     * @param stage
     * @param client       {@link ClientManager} gestore connessione.
     * @param currentScene La scena per bottoni "indietro".
     * @param rimuovi      {@link Runnable} per il refresh.
     * @return {@link VBox} contenente l'elenco dei ristoranti del proprietario.
     */
    public static VBox mieiRistoranti(List<Ristorante> lista, Utente utente, Stage stage, ClientManager client, Scene currentScene, Runnable rimuovi) {
        VBox box = new VBox(16);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: #1a1a1a;");

        Label titolo = new Label("I miei ristoranti");
        titolo.getStyleClass().add("preferiti-titolo");

        VBox boxRistoranti = new VBox(10);

        if (lista == null || lista.isEmpty()) {
            Label empty = new Label("Non possiedi alcun ristorante.");
            empty.getStyleClass().add("preferiti-empty");
            boxRistoranti.getChildren().add(empty);
        } else {
            for (Ristorante ris : lista) {
                HBox card = card(ris, utente, stage, client, currentScene, rimuovi,false, true);
                boxRistoranti.getChildren().add(card);
            }
        }

        ScrollPane scroll = scrollPane(boxRistoranti);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        box.getChildren().addAll(titolo, scroll);
        return box;
    }
    
    /**
     * VBox per la sezione "Le mie recensioni".
     *
     * @param lista      La lista di {@link Recensione} scritte dall'utente.
     * @param ristoranti La lista di {@link Ristorante} relativi alle recensioni.
     * @param utente     {@link Utente} loggato.
     * @param stage
     * @param client     {@link ClientManager} gestore connessione.
     * @param aggiorna   {@link Runnable} per il refresh.
     * @return {@link VBox} formattato contenente le recensioni dell'utente.
     */
    public static VBox mieRecensioni(List<Recensione> lista, List<Ristorante> ristoranti, Utente utente, Stage stage, ClientManager client, Runnable aggiorna) {
        VBox box = new VBox(16);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: #1a1a1a;");

        Label titolo = new Label("Le mie recensioni");
        titolo.getStyleClass().add("preferiti-titolo");

        VBox boxRecensioni = new VBox(10);

        if (lista == null || lista.isEmpty()) {
            Label empty = new Label("Non hai ancora scritto recensioni...");
            empty.getStyleClass().add("preferiti-empty");
            boxRecensioni.getChildren().add(empty);
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Recensione rec = lista.get(i);
                Ristorante ris = ristoranti.get(i);
                boxRecensioni.getChildren().add(miaRecensioneCard(rec, ris, utente, stage, client, aggiorna));
            }
        }

        ScrollPane scroll = scrollPane(boxRecensioni);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        box.getChildren().addAll(titolo, scroll);
        return box;
    }

    /**
     * Card per visualizzare una recensione.
     * 
     * @param rec      La {@link Recensione} da mostrare.
     * @param ris      {@link Ristorante} relativo alla recensione.
     * @param utente   {@link Utente} che ha scritto la recensione.
     * @param stage
     * @param client   {@link ClientManager} gestore connessione.
     * @param aggiorna {@link Runnable} per il refresh.
     * @return {@link VBox} formattato.
     */
    public static VBox miaRecensioneCard(Recensione rec, Ristorante ris, Utente utente, Stage stage, ClientManager client, Runnable aggiorna) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.getStyleClass().add("recensione-box");

        Label risLabel = new Label(ris.getNome());
        risLabel.getStyleClass().add("card-titolo");
        Label stelleLabel = new Label("⭐" + rec.getStelle() + " stelle");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(10, risLabel, spacer, stelleLabel);
        top.setAlignment(Pos.CENTER_LEFT);

        Label testoLabel = new Label(rec.getTesto());
        testoLabel.getStyleClass().add("recensione-testo");
        testoLabel.setWrapText(true);

        card.getChildren().addAll(top, testoLabel);

        if (rec.getRisposta() != null) {
            VBox rispostaBox = new VBox(4);
            rispostaBox.setPadding(new Insets(8, 12, 8, 12));
            rispostaBox.getStyleClass().add("risposta-box");
            Label rispostaTitle = new Label("Risposta del ristoratore:");
            rispostaTitle.getStyleClass().add("risposta-titolo");
            Label rispostaLabel = new Label(rec.getRisposta());
            rispostaLabel.getStyleClass().add("risposta-testo");
            rispostaLabel.setWrapText(true);
            rispostaBox.getChildren().addAll(rispostaTitle, rispostaLabel);
            card.getChildren().add(rispostaBox);
        }

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button dettagliBtn = blackBtn("Dettagli ristorante");
        dettagliBtn.setOnAction(e -> {
            Scene scena = stage.getScene();
            new RistoranteGUI(stage, client, utente, ris, scena).show();
        });

        Button eliminaBtn = blackBtn("Elimina");
        eliminaBtn.setOnAction(e -> {
            try {
                client.send(new Message("removeRecensione", new Object[]{rec.getId()}));
                alert(Alert.AlertType.INFORMATION, "The Knife", "Recensione eliminata con successo.");
                aggiorna.run();
            } catch (ClassNotFoundException | IOException ex) {
                alert(Alert.AlertType.ERROR, "Errore", "Impossibile eliminare la recensione....");
            }
        });

            TextField modificaField = field("Nuovo testo recensione...");
            modificaField.setVisible(false);
            modificaField.setManaged(false);  
            
            Button modificaBtn = blackBtn("Modifica");
            modificaBtn.setOnAction(e -> {
                if (!modificaField.isVisible()) {
                    modificaField.setVisible(true);
                    modificaField.setManaged(true);
                    modificaField.setText(rec.getTesto());
                    modificaBtn.setText("Conferma");
                } else {
                    String nuovoTesto = modificaField.getText().trim();
                    if (nuovoTesto.isEmpty()) return;
                    try {
                        client.send(new Message("modifyRecensione", new Object[]{rec.getId(), nuovoTesto}));
                        alert(AlertType.INFORMATION, "The Knife", "Recensione modificata con successo.");
                        aggiorna.run();
                    } catch (ClassNotFoundException | IOException ex) {
                        alert(AlertType.ERROR, "Recensione", "Impossibile modificare la recensione.");
                    }
                }});        

        actions.getChildren().addAll(dettagliBtn, modificaField, modificaBtn, eliminaBtn);
        card.getChildren().add(actions);
        return card;
    }    

    /**
     * Viene usato per la creazione di scene.
     * 
     * @param box {@link VBox}
     * @return {@link Scene} creata.
     */
    public static Scene makeScene(VBox box) {
        StackPane container = new StackPane(box);
        StackPane.setAlignment(box, Pos.CENTER);
        Scene scene = new Scene(container);
        scene.getStylesheets().add(GUIComponents.class.getResource("/style.css").toExternalForm());
        return scene;
    }    
}
