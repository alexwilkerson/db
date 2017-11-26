import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

class SelectScreen {
    private String user, pass;

    SelectScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefWidth(280);

        Button sqlButton = new Button("SQL Queries");
        sqlButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        sqlButton.setMinWidth(buttons.getPrefWidth());
        sqlButton.setMinHeight(75);
        sqlButton.setOnAction(e -> new QueriesScreen(user, pass, primaryStage));

        Button addPersonButton = new Button("Add Person");
        addPersonButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        addPersonButton.setMinWidth(buttons.getPrefWidth());
        addPersonButton.setMinHeight(75);
        addPersonButton.setOnAction(e -> new AddEmployeeScreen(user, pass, primaryStage));

        Button findJobButton = new Button("Find Job");
        findJobButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        findJobButton.setMinWidth(buttons.getPrefWidth());
        findJobButton.setMinHeight(75);
        findJobButton.setOnAction(e -> new FindJobScreen(user, pass, primaryStage));

        Button findEmployeeButton = new Button("Find Employee");
        findEmployeeButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        findEmployeeButton.setMinWidth(buttons.getPrefWidth());
        findEmployeeButton.setMinHeight(75);

        buttons.getChildren().addAll(sqlButton, addPersonButton, findJobButton, findEmployeeButton);
        Scene buttonScene = new Scene(buttons, 300, 350);
        primaryStage.setScene(buttonScene);
        // This is needed to return exiting to normal.
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

        // CENTER ON SCREEN
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

}
