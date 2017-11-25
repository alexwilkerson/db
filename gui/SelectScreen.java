import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectScreen {
    String user, pass;

    public SelectScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefWidth(280);
        Button sqlButton = new Button("SQL Queries");
        sqlButton.setMinWidth(buttons.getPrefWidth());
        sqlButton.setMinHeight(75);
        sqlButton.setOnAction(e -> {
            new QueriesScreen(user, pass, primaryStage);
        });
        Button addPersonButton = new Button("Add Person");
        addPersonButton.setMinWidth(buttons.getPrefWidth());
        addPersonButton.setMinHeight(75);
        Button findJobButton = new Button("Find Job");
        findJobButton.setMinWidth(buttons.getPrefWidth());
        findJobButton.setMinHeight(75);
        Button findEmployeeButton = new Button("Find Employee");
        findEmployeeButton.setMinWidth(buttons.getPrefWidth());
        findEmployeeButton.setMinHeight(75);
        buttons.getChildren().addAll(sqlButton, addPersonButton, findJobButton, findEmployeeButton);
        Scene buttonScene = new Scene(buttons, 300, 350);
        primaryStage.setScene(buttonScene);
        primaryStage.show();
    }

}
