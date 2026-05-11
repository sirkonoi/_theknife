package theknife.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
    
    public static Scene makeScene(VBox box, double weight, double height) {
        StackPane container = new StackPane(box);
        StackPane.setAlignment(box, Pos.CENTER);
        Scene scene = new Scene(container, weight, height);
        scene.getStylesheets().add(GUIComponents.class.getResource("/style.css").toExternalForm());
        return scene;
    }    
}
