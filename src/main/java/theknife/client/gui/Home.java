package theknife.client.gui;

import java.io.*;
import java.sql.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

        Button profiloBtn = GUIComponents.sidebarBtn("Profilo");
        profiloBtn.setOnAction(e -> {
            stage.setScene(profiloScene());
        });
        Button preferitiBtn = GUIComponents.sidebarBtn("❤ Preferiti");
        preferitiBtn.setOnAction(e -> {
            stage.setScene(preferitiScene());
        });        
        Button recensioniBtn = GUIComponents.sidebarBtn("Le mie recensioni");
        recensioniBtn.setOnAction( e -> {
            Scene currentScene = stage.getScene();
            stage.setScene(mieRecensioniScene(currentScene));
        });

        if (guestHome) {
            preferitiBtn.setDisable(true);
            recensioniBtn.setDisable(true);
        }

        VBox menu = new VBox(4, profiloBtn, preferitiBtn, recensioniBtn);

        if (!guestHome && utente.getRuolo().equals("ristoratore")) {
            Separator sep2 = GUIComponents.separator();
            VBox.setMargin(sep2, new Insets(4, 0, 4, 0));
            Button ristorantiBtn = GUIComponents.sidebarBtn("I miei ristoranti");
            ristorantiBtn.setOnAction(e -> {
                Scene currentScene = stage.getScene();
                stage.setScene(mieiRistorantiScene(utente, currentScene));
            });
            Button aggiungiRisBtn = GUIComponents.sidebarBtn("＋ Nuovo ristorante");
            aggiungiRisBtn.setOnAction( e -> {
                Scene currentScene = stage.getScene();
                stage.setScene(newRestaurantScene(utente, currentScene));
            });
            menu.getChildren().addAll(sep2, ristorantiBtn, aggiungiRisBtn);
        }

        Region spacer = GUIComponents.spacer();
        Button logoutBtn = GUIComponents.logoutButton();

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
            boxRistoranti.getChildren().add(GUIComponents.ristoranteCard(ristorante, info[0], (int)info[1], utente, guest, guestHome, stage, client, stage.getScene())); }

        ComboBox<String> filtroCucina = GUIComponents.tipiCucinaBox(tipi);
        ComboBox<String> filtroPrezzo = GUIComponents.prezzoBox(); 

        Slider[] slider = new Slider[1];
        VBox distanzaBox = GUIComponents.sliderDistanza(slider);    

        Button cercaBtn = GUIComponents.greenBtn("Cerca");
        cercaBtn.setOnAction(e -> {
            String testoCerca = cercaField.getText().trim();
            if (!testoCerca.isEmpty()) {
                try {
                    Message res2 = client.send(new Message("cercaFromNome", new Object[]{testoCerca}));
                    boxRistoranti.getChildren().clear();
                    if (res2.getOp().equals("OK")) {
                        Ristorante ris = (Ristorante) res2.getDati()[0];
                        double[] info = new double[]{0, 0};
                        try {
                            Message risInfo = client.send(new Message("infoRecensioni", new Object[]{ris.getId()}));
                            info = (double[]) risInfo.getDati()[0];
                        } catch (ClassNotFoundException | IOException ex) {}
                        boxRistoranti.getChildren().add(GUIComponents.ristoranteCard(ris, info[0], (int)info[1], utente, guest, guestHome, stage, client, stage.getScene()));
                    } else {
                        Label noRis = new Label("Nessun ristorante trovato.");
                        noRis.getStyleClass().add("preferiti-empty");
                        boxRistoranti.getChildren().add(noRis);
                    }
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Errore nella ricerca per nome.");
                }
                return;
            }

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
                    double[] info = new double[]{0, 0};
                    try {
                        Message risInfo = client.send(new Message("infoRecensioni", new Object[]{ristorante.getId()}));
                        info = (double[]) risInfo.getDati()[0];
                    } catch (ClassNotFoundException | IOException ex) {}
                    boxRistoranti.getChildren().add(GUIComponents.ristoranteCard(ristorante, info[0], (int)info[1], utente, guest, guestHome, stage, client, stage.getScene()));
                }
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
        VBox pref = GUIComponents.preferiti(lista, utente, stage, client, currentScene, () -> {
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

    private Scene newRestaurantScene(Utente user, Scene previousScene) {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        TextField nomeField = GUIComponents.field("Nome Ristorante");
        TextField indirizzoField = GUIComponents.field("Indirizzo");
        TextField cittaField = GUIComponents.field("Citta'");
        TextField nazioneField = GUIComponents.field("Nazione");
        TextField fasciaField = GUIComponents.field("Prezzo medio (es. 50)");
        TextField tipoField = GUIComponents.field("Tipo cucina (es. Italiana, separa i tipi con virgola!)");
        CheckBox delivery = new CheckBox("Delivery");
        delivery.setStyle("-fx-text-fill: white;");
        CheckBox booking  = new CheckBox("Prenotazione online");
        booking.setStyle("-fx-text-fill: white;");
        HBox selRow = new HBox(20, delivery, booking);
        selRow.setAlignment(Pos.CENTER);

        Label err = GUIComponents.errorLabel();

        Button addBtn  = GUIComponents.greenBtn("Aggiungi ristorante");
        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        addBtn.setOnAction(e -> {
            GUIComponents.hideError(err);
            String nome = nomeField.getText().trim();
            Luogo luogo = null;
            try {
                luogo = new Luogo(indirizzoField.getText().trim(), cittaField.getText().trim(), nazioneField.getText().trim());
                if (!luogo.luogoExists()) {
                    GUIComponents.showError(err, "Inserisci un indirizzo esistente..");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int fascia = 0;
            try {
                fascia = Integer.parseInt(fasciaField.getText().trim().replace("€", "").trim());
            } catch (NumberFormatException ne) {
                GUIComponents.showError(err, "Inserisci un valore numerico per la fascia di prezzo. (Esempio 50)");
                return;
            }            
            String tipo = tipoField.getText().trim();   
            List<String> tipi = List.of(tipo.split(","));                    
            boolean deliverySel = delivery.isSelected();
            boolean bookingSel = booking.isSelected();

            if (nome.isEmpty() || luogo == null || tipo.isEmpty()) {
                GUIComponents.showError(err, "Compila tutti i campi.");
                return;
            } 

            try {
                Ristorante ris = new Ristorante(0, nome, luogo, fascia, deliverySel, bookingSel, utente.getId());
                Message res = client.send(new Message("addRistorante", new Object[]{ris, tipi.toArray(new String[0])}));
                if (res.getOp().equals("OK")) {
                    GUIComponents.alert(Alert.AlertType.INFORMATION, "Ristorante", "Ristorante aggiunto con successo!");
                    stage.setScene(previousScene);
                } else {
                    GUIComponents.showError(err, "Aggiunta fallita. Riprova.");
                    GUIComponents.alert(Alert.AlertType.INFORMATION, "Ristorante", "Impossibile aggiungere il ristorante.");

                }
            } catch (IOException | ClassNotFoundException ex) {
                GUIComponents.showError(err, "Errore di connessione...");
            }
        });
        layout.getChildren().addAll(GUIComponents.miniLogo(), nomeField, indirizzoField, cittaField, nazioneField, fasciaField, tipoField, selRow, err, addBtn, backBtn);
        return GUIComponents.makeScene(layout, previousScene.getWidth(), previousScene.getHeight());        
    }

    private Scene mieiRistorantiScene(Utente utente, Scene previousScene) {
    try {
        Message res = client.send(new Message("getRistorantiUtente", new Object[]{utente.getId()}));
        List<Ristorante> lista = (List<Ristorante>) res.getDati()[0];

        VBox pref = GUIComponents.mieiRistoranti(lista, utente, stage, client, previousScene, () -> {
            stage.setScene(mieiRistorantiScene(utente, previousScene));
        });

        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> {
            stage.setScene(previousScene);
        });
        
        pref.getChildren().add(backBtn);

        double w = stage.isShowing() ? stage.getWidth() : WIDTH;
        double h = stage.isShowing() ? stage.getHeight() : HEIGHT;
        Scene scene = new Scene(pref, w, h);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;

    } catch (ClassNotFoundException | IOException ex) {
        System.out.println("Errore nel caricare i tuoi ristoranti.");
        GUIComponents.alert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare i ristoranti.");
        return stage.getScene();
        }
    }   

    private Scene mieRecensioniScene(Scene previousScene) {
    try {
        Message res = client.send(new Message("getRecensioniUtente", new Object[]{utente.getId()}));
        List<Recensione> recensioni = (List<Recensione>) res.getDati()[0];
        List<Ristorante> ristoranti = (List<Ristorante>) res.getDati()[1];

        VBox content = GUIComponents.mieRecensioni(recensioni, ristoranti, utente, stage, client, () -> {
            stage.setScene(mieRecensioniScene(previousScene));
        });

        Button backBtn = GUIComponents.blackBtn("↤ Indietro");
        backBtn.setOnAction(e -> {
            stage.setScene(previousScene);
        });
        content.getChildren().add(0, backBtn);

        double w = stage.isShowing() ? stage.getWidth() : WIDTH;
        double h = stage.isShowing() ? stage.getHeight() : HEIGHT;
        Scene scene = new Scene(content, w, h);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return scene;
    } catch (ClassNotFoundException | IOException ex) {
        GUIComponents.alert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare le recensioni.");
        return stage.getScene();
     }
    }
}