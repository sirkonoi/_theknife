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

public class RistoranteGUI implements GUIBasics {

    private Stage stage;
    private ClientManager client;
    private Utente utente;
    private Guest guest;
    private boolean guestHome;
    private Ristorante ristorante;
    private Scene previousScene;

    public RistoranteGUI(Stage stage, ClientManager client, Utente utente, Ristorante ristorante, Scene previousScene) {
        this.stage = stage;
        this.client = client;
        this.utente = utente;
        this.ristorante = ristorante;
        this.previousScene = previousScene;
        this.guestHome = false;
    }

    public RistoranteGUI(Stage stage, ClientManager client, Guest guest, Ristorante ristorante, Scene previousScene) {
        this.stage = stage;
        this.client = client;
        this.guest = guest;
        this.ristorante = ristorante;
        this.previousScene = previousScene;
        this.guestHome = true;
    }
}