import com.sun.deploy.util.StringUtils;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class FindJobScreen {
    private String user, pass;
    private Connection c;
    private List<String> selectedSkills = new ArrayList<>();

    FindJobScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));

        VBox leftSide = new VBox(10);
        leftSide.setAlignment(Pos.CENTER);

        Text instructions = new Text("Select your qualifications.");

        ListView<String> skillList = new ListView<>();
        skillList.setMinHeight(520);
        skillList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        skillList.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedSkills.add("'" + item + "'");
                } else {
                    selectedSkills.remove("'" + item + "'");
                }
                System.out.println(StringUtils.join(selectedSkills, ", "));
            });
            return observable;
        }));

        ObservableList<String> skills = FXCollections.observableArrayList();
        skillList.setItems(skills);

        Button search = new Button("Find Jobs");
        search.setOnAction(e -> searchJobs());

        leftSide.getChildren().addAll(instructions, skillList, search);

        borderPane.setLeft(leftSide);

        ListView<String> jobList = new ListView<>();
        borderPane.setCenter(jobList);
        BorderPane.setMargin(jobList, new Insets(0, 0, 0, 10));

        Scene scene = new Scene(borderPane, 640, 600);
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

        String SQL = "SELECT ks_title FROM knowledge_skill";
        try {
            c = DBConnect.connect(user, pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                String current = rs.getString("ks_title");
                ObservableList<String> list = FXCollections.observableArrayList(current);
                skillList.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            new SelectScreen(user, pass, primaryStage);
        }

    }

    private void searchJobs() {

    }

}
