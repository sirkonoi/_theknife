package theknife.client.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import theknife.Message;
import theknife.client.ClientManager;
import theknife.server.DBManager;
import theknife.server.RestaurantManager;
import theknife.server.models.Recensione;
import theknife.server.models.Ristorante;
import theknife.server.models.Utente;

public class Home implements GUIBasics {

    private Stage stage;
    private ClientManager client;
    private Utente utente; // null se guest

    public Home(Stage stage, ClientManager client, Utente utente) {
        this.stage = stage;
        this.client = client;
        this.utente = utente;
    }

    public void show() throws SQLException, IOException {
        stage.setScene(homeScene());
        stage.setTitle("TheKnife");
        stage.show();
    }

    private Scene homeScene() throws SQLException, IOException {

        Message res;


        HBox root = new HBox();
        root.setStyle("-fx-background-color: #1a1a1a;");

        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(190);
        sidebar.setMinWidth(160);
        sidebar.setPadding(new Insets(24, 14, 24, 14));
        sidebar.setStyle("-fx-background-color: #222922;" + "-fx-border-color: #2e3d2e;" + "-fx-border-width: 0 1 0 0;");

        Circle avatar = new Circle(26);
        avatar.setStyle("-fx-fill: #2a2a2a; -fx-stroke: #4caf50; -fx-stroke-width: 2;");
        StackPane avatarBox = new StackPane(avatar);
        avatarBox.setAlignment(Pos.CENTER);

        String nomeUtente = (utente != null) ? utente.getNome() + " " + utente.getCognome() : "Guest";
        Label nomeLabel = new Label(nomeUtente);
        nomeLabel.setStyle("-fx-text-fill: #f0f0f0; -fx-font-size: 13; -fx-font-weight: bold;");
        nomeLabel.setWrapText(true);
        nomeLabel.setMaxWidth(Double.MAX_VALUE);

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #2e3d2e;");
        VBox.setMargin(sep1, new Insets(4, 0, 4, 0));

        Button profiloBtn = sidebarBtn("👤 Profilo");
        Button preferitiBtn = sidebarBtn("💖 Preferiti");
        Button recensioniBtn = sidebarBtn("✍️ Le mie recensioni");

        if (utente == null) {
            preferitiBtn.setDisable(true);
            recensioniBtn.setDisable(true);
        }

        VBox menu = new VBox(4, profiloBtn, preferitiBtn, recensioniBtn);

        if (utente != null && utente.getRuolo().equals("ristoratore")) {
            Separator sep2 = new Separator();
            sep2.setStyle("-fx-background-color: #2e3d2e;");
            VBox.setMargin(sep2, new Insets(4, 0, 4, 0));
            Button mieiBtn = sidebarBtn("🍴 I miei ristoranti");
            Button recRicevuteBtn = sidebarBtn("💬 Recensioni ricevute");
            Button aggiungiBtn = sidebarBtn("＋ Aggiungi ristorante");
            menu.getChildren().addAll(sep2, mieiBtn, recRicevuteBtn, aggiungiBtn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logoutBtn = sidebarBtn("⛔ Esci");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e05252;" + "-fx-font-size: 13; -fx-background-radius: 6; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> new Welcome(stage, client).show());

        sidebar.getChildren().addAll(avatarBox, nomeLabel, sep1, menu, spacer, logoutBtn);

        VBox content = new VBox(16);
        content.setPadding(new Insets(20));
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = GUIComponents.field("Cerca ristoranti...");
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ComboBox<String> filtroCucina = new ComboBox<>();
        filtroCucina.setPromptText("Cucina");
        filtroCucina.setPrefWidth(110);

        List<String> tipi = new ArrayList<>();
        try {
            res = client.send(new Message("getAllTipi", new Object[]{}));
            tipi = (List<String>) res.getDati()[0];
            filtroCucina.getItems().addAll(tipi);
        } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei tipi...");}        


        ComboBox<String> filtroPrezzo = new ComboBox<>();
        filtroPrezzo.setPromptText("Prezzo");
        filtroPrezzo.getItems().addAll("< 20€", "20-50€", "> 50€");
        filtroPrezzo.setPrefWidth(100);

        CheckBox deliveryCheck = new CheckBox("Delivery");
        CheckBox prenotazioneCheck = new CheckBox("Prenotazione");
        deliveryCheck.setStyle("-fx-text-fill: #f0f0f0;");
        prenotazioneCheck.setStyle("-fx-text-fill: #f0f0f0;");

        Button cercaBtn = GUIComponents.greenBtn("Cerca");

        topBar.getChildren().addAll(searchField, filtroCucina, filtroPrezzo, deliveryCheck, prenotazioneCheck,
                cercaBtn);

        VBox boxRistoranti = new VBox(10);

        String indirizzo = (utente != null) ? utente.getDomicilio() : "Milano"; // default per guest
        List<Ristorante> listaRistoranti = new ArrayList<>();
        try {
            res = client.send(new Message("cercaVicini", new Object[]{indirizzo, 30}));
            listaRistoranti = (List<Ristorante>) res.getDati()[0];

        } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei ristoranti vicini..");}
       
        for(Ristorante ristorante : listaRistoranti) {
            double[] info = null;
            try {
                res = client.send(new Message("infoRecensioni", new Object[]{ristorante.getId()}));
                info = (double[]) res.getDati()[0];
            } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei ristoranti vicini..");}            
            boxRistoranti.getChildren().add(ristoranteCard(ristorante.getNome(), ristorante.getLuogo().getIndirizzo(), "PLACEHOLDER", ristorante.getFasciaPrezzo(), info[0], (int)info[1]));
        }

        ScrollPane scroll = new ScrollPane(boxRistoranti);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: #1a1a1a; -fx-background-color: #1a1a1a;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        content.getChildren().addAll(topBar, scroll);
        root.getChildren().addAll(sidebar, content);

        double w = stage.isShowing() ? stage.getWidth() : WIDTH;
        double h = stage.isShowing() ? stage.getHeight() : HEIGHT;
        Scene scene = new Scene(root, w, h);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;
    }

    private HBox ristoranteCard(String nome, String citta, String cucina, int prezzo, double media, int numRec) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setAlignment(Pos.CENTER_LEFT);

        String base = "-fx-background-color: #222922; -fx-background-radius: 8;" +
                "-fx-border-color: #2e3d2e; -fx-border-width: 1; -fx-border-radius: 8; -fx-cursor: hand;";
        String hover = "-fx-background-color: #263026; -fx-background-radius: 8;" +
                "-fx-border-color: #4caf50; -fx-border-width: 1; -fx-border-radius: 8; -fx-cursor: hand;";
        card.setStyle(base);
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e -> card.setStyle(base));

        // info
        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nomeLabel = new Label(nome);
        nomeLabel.setStyle("-fx-text-fill: #f0f0f0; -fx-font-size: 14; -fx-font-weight: bold;");
        Label dettagliLabel = new Label(citta + " · " + cucina + " · " + prezzo + "€");
        dettagliLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12;");
        Label stelleLabel = new Label("⭐ " + (media == 0 ? 0 : media) +  " (" + numRec +  (numRec == 1 ? " recensione" : " recensioni") );
        stelleLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12;");

        info.getChildren().addAll(nomeLabel, dettagliLabel, stelleLabel);

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button dettagliBtn = GUIComponents.blackBtn("Dettagli");
        dettagliBtn.setOnAction(e -> {
            /* TODO: apri RistoranteGUI */ });
        actions.getChildren().add(dettagliBtn);

        if (utente != null) {
            Button prefBtn = GUIComponents.blackBtn("❤");
            prefBtn.setOnAction(e -> {
                /* TODO: aggiungiPreferito */ });
            actions.getChildren().add(prefBtn);

            if (utente.getRuolo().equals("utente")) {
                Button recBtn = GUIComponents.greenBtn("Recensisci");
                recBtn.setOnAction(e -> {
                    /* TODO: aggiungiRecensione */ });
                actions.getChildren().add(recBtn);
            }
        }

        card.getChildren().addAll(info, actions);
        return card;
    }

    private Button sidebarBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(9, 12, 9, 12));
        String base = "-fx-background-color: transparent; -fx-text-fill: #f0f0f0; -fx-font-size: 13; -fx-background-radius: 6; -fx-cursor: hand;";
        String hover = "-fx-background-color: #2e3d2e; -fx-text-fill: #4caf50; -fx-font-size: 13; -fx-background-radius: 6; -fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }
}