import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;

class AddPersonScreen {
    private String user, pass;

    AddPersonScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

        Text sceneTitle = new Text("Add Person");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);


        Text chooseCompanyText = new Text("Choose company:");
        gridPane.add(chooseCompanyText, 0,1,1,1);
        ComboBox<String> companyComboBox = new ComboBox<>();
        gridPane.add(companyComboBox, 1, 1, 1, 1);

        Text nameText = new Text("Name:");
        gridPane.add(nameText, 0, 2,1,1);

        TextField nameField = new TextField();
        gridPane.add(nameField, 1,2,1,1);

        Scene scene = new Scene(gridPane, 640, 600);
        primaryStage.setScene(scene);
        // return to select screen on close
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            new SelectScreen(user, pass, primaryStage);
        });
        primaryStage.show();

        // CENTER ON SCREEN
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        // POPULATE DATA
        Connection c;
        String SQL = "SELECT comp_name FROM company";
        try {
            c = DBConnect.connect(user, pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                String current = rs.getString("comp_name");
                ObservableList<String> list = FXCollections.observableArrayList(current);
                companyComboBox.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            new SelectScreen(user, pass, primaryStage);
        }
    }
}
