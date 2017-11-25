import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private String user, pass;

    // MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Login scene

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

        Text sceneTitle = new Text("Pending Matters");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("User Name:");
        gridPane.add(userNameLabel, 0, 1);

        TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        gridPane.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        userTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                pwBox.requestFocus();
            }
        });
        pwBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (userTextField.getText().trim().equals("awilkers") && pwBox.getText().equals("zT7RXLfP")) {
                    user = "awilkers";
                    pass = "zT7RXLfP";
                    new SelectScreen(user, pass, stage);
                } else if (userTextField.getText().trim().equals("kbongcas") && pwBox.getText().equals("9TTtPT97")) {
                    user = "kbongcas";
                    pass = "9TTtPT97";
                    new SelectScreen(user, pass, stage);
                } else {
                    AlertBox.display("Wrong Credentials", "Username or Password incorrect.");
                    pwBox.setText("");
                }
            }
        });

        gridPane.add(pwBox, 1, 2);

        stage.setTitle("Pending Matters Job Corp");
        Scene loginScene = new Scene(gridPane, 640, 480);

        final KeyCodeCombination keyCombinationAlex = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
        final KeyCodeCombination keyCombinationKevin = new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_ANY);
        loginScene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (keyCombinationAlex.match(e)) {
                user = "awilkers";
                pass = "zT7RXLfP";
                new SelectScreen(user, pass, stage);
            }
            if (keyCombinationKevin.match(e)) {
                user = "kbongcas";
                pass = "9TTtPT97";
                new SelectScreen(user, pass, stage);
            }
        });

        stage.setScene(loginScene);
        stage.show();
    }

}
