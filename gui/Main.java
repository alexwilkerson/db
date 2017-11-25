import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    // MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Login scene

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(75,25,25,25));

        Image logo = new Image(new FileInputStream("./logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setX(270);
        logoView.setFitHeight(200);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);
        gridPane.add(logoView,0,0,2, 1);
        GridPane.setHalignment(logoView, HPos.CENTER);

        Text sceneTitle = new Text("Pending Matters");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 32));
        gridPane.add(sceneTitle, 0, 1, 2, 1);

        Label userNameLabel = new Label("User Name:");
        gridPane.add(userNameLabel, 0, 2);

        TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        gridPane.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        userTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                pwBox.requestFocus();
            }
        });
        pwBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (userTextField.getText().trim().equals("awilkers") && pwBox.getText().equals("zT7RXLfP")) {
                    new SelectScreen("awilkers", "zT7RXLfP", stage);
                } else if (userTextField.getText().trim().equals("kbongcas") && pwBox.getText().equals("9TTtPT97")) {
                    new SelectScreen("kbongcas", "9TTtPT97", stage);
                } else {
                    AlertBox.display("Wrong Credentials", "Username or Password incorrect.");
                    pwBox.setText("");
                }
            }
        });

        gridPane.add(pwBox, 1, 3);

        stage.setTitle("Pending Matters Job Co.");
        Scene loginScene = new Scene(gridPane, 640, 480);

        // These shortcuts are simply to make dev easier
        final KeyCodeCombination keyCombinationAlex = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
        final KeyCodeCombination keyCombinationKevin = new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_ANY);
        loginScene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (keyCombinationAlex.match(e)) {
                new SelectScreen("awilkers", "zT7RXLfP", stage);
            }
            if (keyCombinationKevin.match(e)) {
                new SelectScreen("kbongcas", "9TTtPT97", stage);
            }
        });

        stage.setScene(loginScene);
        stage.show();

        // CENTER ON SCREEN
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

}
