package theknife.client.gui;

import java.io.*;
import java.sql.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import theknife.*;
import theknife.client.ClientManager;
import theknife.server.models.*;

public class Home implements GUIBasics {

    private Stage stage;
    private ClientManager client;
    private Utente utente;
    private Guest guest;
    private boolean guestHome = false;

    public Home(Stage stage, ClientManager client, Utente utente) {
        this.stage = stage;
        this.client = client;
        this.utente = utente;
    }

    public Home(Stage stage, ClientManager client, Guest guest) {
        this.stage = stage;
        this.client = client;
        this.guest = guest;
        guestHome = true;
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

        VBox sidebar = GUIComponents.sidebar();

        StackPane avatar = GUIComponents.avatar(guestHome ? "Guest" : utente.getUsername());
        Label usernameLabel;

        if(!guestHome) { usernameLabel = new Label(utente.getUsername());}
        else { usernameLabel = new Label("Guest"); }

        Separator sep1 = GUIComponents.separator();
        VBox.setMargin(sep1, new Insets(4, 0, 4, 0));

        Button profiloBtn = GUIComponents.sidebarBtn("👤 Profilo");
        profiloBtn.setOnAction(e -> {
            stage.setScene(profiloScene());
        });
        Button preferitiBtn = GUIComponents.sidebarBtn("❤ Preferiti");
        preferitiBtn.setOnAction(e -> {
            stage.setScene(preferitiScene());
        });        
        Button recensioniBtn = GUIComponents.sidebarBtn("🖋️ Le mie recensioni");

        if (guestHome) {
            preferitiBtn.setDisable(true);
            recensioniBtn.setDisable(true);
        }

        VBox menu = new VBox(4, profiloBtn, preferitiBtn, recensioniBtn);

        if (!guestHome && utente.getRuolo().equals("ristoratore")) {
            Separator sep2 = GUIComponents.separator();
            VBox.setMargin(sep2, new Insets(4, 0, 4, 0));
            Button ristorantiBtn = GUIComponents.sidebarBtn("👨‍🍳 I miei ristoranti");
            Button recRisBtn = GUIComponents.sidebarBtn("Recensioni ricevute");
            Button aggiungiRisBtn = GUIComponents.sidebarBtn("＋ Nuovo ristorante");
            menu.getChildren().addAll(sep2, ristorantiBtn, recRisBtn, aggiungiRisBtn);
        }

        Region spacer = GUIComponents.spacer();
        Button logoutBtn = GUIComponents.logoutButton(stage, client);

        sidebar.getChildren().addAll(avatar, usernameLabel, sep1, menu, spacer, logoutBtn);

        VBox mainBox = new VBox(16);
        mainBox.setPadding(new Insets(20));
        HBox.setHgrow(mainBox, Priority.ALWAYS);

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        TextField cercaField = GUIComponents.field("Cerca ristoranti...");
        cercaField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cercaField, Priority.ALWAYS);

        CheckBox delivery = GUIComponents.filterCheckBox("Delivery");
        CheckBox prenotazione = GUIComponents.filterCheckBox("Prenotazione");

        List<String> tipi = new ArrayList<>();
        try {
            res = client.send(new Message("getAllTipi", new Object[]{}));
            tipi = (List<String>) res.getDati()[0];
        } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei tipi...");}        
       
        VBox boxRistoranti = new VBox(10);

        String indirizzo = (guestHome) ? guest.getDomicilio() : utente.getDomicilio();
        List<Ristorante> listaRistoranti = new ArrayList<>();
        
        try {
            res = client.send(new Message("filtra", new Object[]{30, null, null, null, null, indirizzo}));
            listaRistoranti = (List<Ristorante>) res.getDati()[0];

        } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei ristoranti vicini..");}
       
        for(Ristorante ristorante : listaRistoranti) {
            double[] info = null;
            try {
                res = client.send(new Message("infoRecensioni", new Object[]{ristorante.getId()}));
                info = (double[]) res.getDati()[0];
            } catch (ClassNotFoundException | IOException e1) { System.out.println("Errore nel caricare la lista dei ristoranti vicini..");}            
            boxRistoranti.getChildren().add(GUIComponents.ristoranteCard(ristorante, info[0], (int)info[1], utente, guest, guestHome, stage, client, stage.getScene()));        }
        //bisogna inserire tipi ristorante

        ComboBox<String> filtroCucina = GUIComponents.tipiCucinaBox(tipi);
        ComboBox<String> filtroPrezzo = GUIComponents.prezzoBox(); 

        Slider[] slider = new Slider[1];
        VBox distanzaBox = GUIComponents.sliderDistanza(slider);    

        Button cercaBtn = GUIComponents.greenBtn("Cerca");
        cercaBtn.setOnAction(e -> {
            String cucina = (filtroCucina.getValue() == null || filtroCucina.getValue().equals("Nessuna")) ? null : filtroCucina.getValue();
            String prezzo = (filtroPrezzo.getValue() == null || filtroPrezzo.getValue().equals("Nessuna")) ? null : filtroPrezzo.getValue();
            Boolean deliveryVal = delivery.isSelected() ? true : null;
            Boolean prenotazioneVal = prenotazione.isSelected() ? true : null;
            int raggio = (int) slider[0].getValue();

            try {
                Message res2 = client.send(new Message("filtra", new Object[]{raggio, cucina, prezzo, deliveryVal, prenotazioneVal, indirizzo}));
                List<Ristorante> nuova = (List<Ristorante>) res2.getDati()[0];

                boxRistoranti.getChildren().clear();

                for (Ristorante ristorante : nuova) {
                    double[] info = null;
                    try {
                        Message risInfo = client.send(new Message("infoRecensioni", new Object[]{ristorante.getId()}));
                        info = (double[]) risInfo.getDati()[0];
                    } catch (ClassNotFoundException | IOException ex) { info = new double[]{0, 0}; }
                    boxRistoranti.getChildren().add(GUIComponents.ristoranteCard(ristorante, info[0], (int)info[1], utente, guest, guestHome, stage, client, stage.getScene()));                }
            } catch (ClassNotFoundException | IOException ex) {
                System.out.println("Errore, il filtraggio non e' riuscito...");
            }
        });  

        topBar.getChildren().addAll(cercaField, distanzaBox, filtroCucina, filtroPrezzo, delivery, prenotazione, cercaBtn);

        ScrollPane scroll = GUIComponents.scrollPane(boxRistoranti);
        mainBox.getChildren().addAll(topBar, scroll);
        root.getChildren().addAll(sidebar, mainBox);

        double w = stage.isShowing() ? stage.getWidth() : WIDTH;
        double h = stage.isShowing() ? stage.getHeight() : HEIGHT;
        Scene scene = new Scene(root, w, h);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;
    }

    private Scene profiloScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #1a1a1a;");
        String nome = guestHome ? "Guest" : utente.getNome() + " " + utente.getCognome();
        String ruolo = guestHome ? "Guest" : utente.getRuolo();
        String username = guestHome ? "Guest" : utente.getUsername();
        String domicilio = guestHome ? guest.getDomicilio().toUpperCase() : utente.getDomicilio().toUpperCase();

        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> {
            try { show(); } catch (SQLException | IOException ex) { ex.printStackTrace(); }
        });

        layout.getChildren().addAll(GUIComponents.profiloCard(nome, ruolo, username, domicilio), backBtn);
        
        double w = stage.isShowing() ? stage.getWidth()  : WIDTH;
        double h = stage.isShowing() ? stage.getHeight() : HEIGHT;
        Scene scene = new Scene(new StackPane(layout), w, h);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;
    }

    private Scene preferitiScene() {
    try {
        Message res = client.send(new Message("getPreferitiUtente", new Object[]{utente.getId()}));
        List<Ristorante> lista = (List<Ristorante>) res.getDati()[0];

        Scene currentScene = stage.getScene();
        VBox pref = GUIComponents.preferitiView(lista, utente, stage, client, currentScene, () -> {
            stage.setScene(preferitiScene());
        });

        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> {
            try { show(); } catch (SQLException | IOException ex) { ex.printStackTrace(); }
        });
        
        pref.getChildren().add(backBtn);

        double w = stage.isShowing() ? stage.getWidth() : WIDTH;
        double h = stage.isShowing() ? stage.getHeight() : HEIGHT;
        Scene scene = new Scene(pref, w, h);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;

    } catch (ClassNotFoundException | IOException ex) {
        System.out.println("Errore nel caricare i preferiti.");
        GUIComponents.alert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare i preferiti.");
        return stage.getScene();
    }
}
}