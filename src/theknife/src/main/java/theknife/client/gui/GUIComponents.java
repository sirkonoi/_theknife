package theknife.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class GUIComponents implements GUIBasics {
    public static TextField field(String text) {
        TextField f = new TextField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        f.setMaxWidth(FIELD_WIDTH);
        return f;
    }

    public static PasswordField passField(String text) {
        PasswordField f = new PasswordField();
        f.setPromptText(text);
        f.setPrefWidth(FIELD_WIDTH);
        f.setMaxWidth(FIELD_WIDTH);
        return f;
    }

    public static Button greenBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(BTN_WIDTH);
        b.getStyleClass().add("btn-green");
        return b;
    }

    public static Button blackBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(BTN_WIDTH);
        b.getStyleClass().add("btn-black");
        return b;
    }

    public static Button logoutButton(Stage stage, ClientManager client) {

        Button btn = new Button("⛔ Esci");
        btn.getStyleClass().add("logout-btn");

        btn.setOnAction(e -> System.exit(0));

        return btn;
    }   
    
    public static Button sidebarBtn(String testo) {
        Button b = new Button(testo);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.getStyleClass().add("sidebar-btn");
        return b;
    }  

    public static Label errorLabel() {
        Label l = new Label();
        l.getStyleClass().add("error-label");
        l.setVisible(false);
        l.setManaged(false);
        return l;
    }

    public static void showError(Label l, String msg) {
        l.setText("Errore: " + msg);
        l.setVisible(true);
        l.setManaged(true);
    }

    public static void hideErr(Label l) {
        l.setVisible(false);
        l.setManaged(false);
    }

    public static VBox logo() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        try {
            ImageView img = new ImageView(new Image(GUIComponents.class.getResourceAsStream("/logo.png")));
            img.setFitHeight(70);
            img.setPreserveRatio(true);
            box.getChildren().add(img);
        } catch (Exception ignored) {}
        return box;
    }
    
    public static VBox miniLogo() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        try {
            ImageView img = new ImageView(new Image(GUIComponents.class.getResourceAsStream("/logo.png")));
            img.setFitHeight(30);
            img.setPreserveRatio(true);
            box.getChildren().add(img);
        } catch (Exception ignored) {}
        return box;
    }  
    
    public static VBox sidebar() {
        VBox sidebar = new VBox(12);
        sidebar.getStyleClass().add("side-bar");
        return sidebar;
    }

    public static Label username(String username) {

        Label label = new Label(username);
        label.getStyleClass().add("username");

        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);

        return label;
    }    

    public static Separator separator() {

        Separator sep = new Separator();
        sep.getStyleClass().add("separator");

        return sep;
    }
    
    public static Region spacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }   
    
    public static ComboBox<String> tipiCucinaBox(List<String> lista) {

        ComboBox<String> tipi = new ComboBox<>();
        tipi.setPromptText("Cucina");
        tipi.setPrefWidth(110);

        tipi.getItems().add("Nessuna");
        tipi.getItems().setAll(lista);

        tipi.getStyleClass().add("combobox");

        return tipi;
    }  
    
    public static ComboBox<String> prezzoBox() {

        ComboBox<String> prezzo = new ComboBox<>();
        prezzo.setPromptText("Prezzo");
        prezzo.setPrefWidth(100);

        prezzo.getItems().addAll("< 20€", "20-50€", "50-100€", "> 100€");

        prezzo.getStyleClass().add("combobox");

        return prezzo;
    }  
    
    public static CheckBox filterCheckBox(String testo) {

        CheckBox check = new CheckBox(testo);
        check.getStyleClass().add("checkbox");

        return check;
    }  
    
    public static ScrollPane scrollPane(Node content) {

        ScrollPane scroll = new ScrollPane(content);

        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scroll.getStyleClass().add("scroll");

        VBox.setVgrow(scroll, Priority.ALWAYS);

        return scroll;
    }

    //rivedere x GUEST
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
                alert(Alert.AlertType.INFORMATION, "Preferiti", "Ristorante aggiunto!");                
            } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nell'aggiungere il preferito..."); alert(Alert.AlertType.ERROR, "Errore", "Impossibile salvare il preferito.");}               
            });
        }

        card.getChildren().addAll(info, actions);
        return card;
    }

    public static Alert alert(Alert.AlertType tipo, String titolo,  String testo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(testo);
        alert.showAndWait();
        return alert;
    }

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

        card.getChildren().addAll(avatarBox, nameBox, sep,infoRow("👤", "Username", username), infoRow("📍", "Domicilio", domicilio));
        return card;
    }

    public static HBox infoRow(String emoji, String titolo, String testo) {
        Label emojiLabel  = new Label(emoji);
        Label titoloLabel = new Label(titolo + ": ");
        titoloLabel.getStyleClass().add("info-titolo");
        Label testoLabel = new Label(testo);
        testoLabel.getStyleClass().add("info-valore");
        HBox row = new HBox(8, emojiLabel, titoloLabel, testoLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }   
    
    public static VBox preferiti(List<Ristorante> lista, Utente utente, Stage stage, ClientManager client, Scene currentScene, Runnable onRimuovi) {
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
                HBox card = preferitoCard(ris, utente, stage, client, currentScene, onRimuovi);
                boxRistoranti.getChildren().add(card);
            }
        }

        ScrollPane scroll = scrollPane(boxRistoranti);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        box.getChildren().addAll(titolo, scroll);
        return box;
    }

    public static HBox preferitoCard(Ristorante r, Utente utente, Stage stage, ClientManager client, Scene currentScene, Runnable onRimuovi) {
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
        info.getChildren().addAll(nomeLabel, dettagliLabel);

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button dettagliBtn = blackBtn("Dettagli");
        dettagliBtn.setOnAction(e -> new RistoranteGUI(stage, client, utente, r, currentScene).show());

        Button rimuoviBtn = blackBtn("Rimuovi");
        rimuoviBtn.setOnAction(e -> {
            try {
                client.send(new Message("removePreferiti", new Object[]{utente.getId(), r.getId()}));
                alert(Alert.AlertType.INFORMATION, "Preferiti", "Ristorante rimosso dai preferiti.");
                onRimuovi.run();
            } catch (ClassNotFoundException | IOException ex) {
                System.out.println("Errore nella rimozione del preferito.");
                alert(Alert.AlertType.ERROR, "Errore", "Impossibile rimuovere il ristorante.");
            }
        });

        actions.getChildren().addAll(dettagliBtn, rimuoviBtn);
        card.getChildren().addAll(info, actions);
        return card;
    }    

    //RISTORANTE GUI

    public static VBox infoRistoranteBox(Ristorante ristorante) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.getStyleClass().add("info-card");
        card.getChildren().addAll(infoRow("🌍", "Indirizzo", ristorante.getLuogo().getIndirizzo()), infoRow("💶", "Prezzo medio", ristorante.getFasciaPrezzo() + "€"), infoRow("🚚", "Delivery", ristorante.isDelivery() ? "✅ Effettuato" : "❌ Non effettuato"), infoRow("📅", "Prenotazione", ristorante.isPrenotazioneOnline() ? "✅ Disponibile" : "❌ Non disponibile"));
        return card;
    }

    public static VBox recensioneBox(Recensione rec, boolean isOwner, boolean guestHome, Utente utente, ClientManager client, Runnable onRicarica) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.getStyleClass().add("recensione-box");

        Label autoreLabel = new Label("Utente #" + rec.getIdRecensore());
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
                    alert(AlertType.INFORMATION, "Recensione", "Risposta aggiunta con successo.");
                    onRicarica.run();
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nell'inviare la risposta.");
                    alert(AlertType.ERROR, "Recensione", "Impossibile aggiungere la risposta. Riprova.");

                }
            });
            card.getChildren().addAll(rispostaField, rispondiBtn);
        }

        if (!guestHome && utente.getId() == rec.getIdRecensore()) {
            Button eliminaBtn = blackBtn("Elimina");
            eliminaBtn.setOnAction(e -> {
                try {
                    client.send(new Message("removeRecensione", new Object[]{rec.getId()}));
                    onRicarica.run();
                    alert(AlertType.INFORMATION, "Recensione", "Recensione eliminata con successo.");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nell'eliminare la recensione.");
                    alert(AlertType.ERROR, "Recensione", "Impossibile eliminare la recensione. Riprova.");
                }
            });
            card.getChildren().add(eliminaBtn);
        }

        return card;
    }

    public static VBox formRecensione(Ristorante ristorante, Utente utente, ClientManager client, Runnable onInvia) {
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
                    hideErr(err);
                    onInvia.run();
                    alert(AlertType.INFORMATION, "Recensione", "Recensione aggiunta con successo.");
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

    public static Scene makeScene(VBox box, double w, double h) {
        StackPane container = new StackPane(box);
        StackPane.setAlignment(box, Pos.CENTER);
        Scene scene = new Scene(container, w, h);
        scene.getStylesheets().add(GUIComponents.class.getResource("/style.css").toExternalForm());
        return scene;
    }    
}
