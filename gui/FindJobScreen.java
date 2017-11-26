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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class FindJobScreen {
    private Stage primaryStage;
    private String user, pass;
    private Connection c;
    private List<String> selectedSkills = new ArrayList<>();
    private ListView<VBox> jobList = new ListView<>();

    FindJobScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;
        this.primaryStage = primaryStage;

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

        borderPane.setCenter(jobList);
        BorderPane.setMargin(jobList, new Insets(0, 0, 0, 10));

        Scene scene = new Scene(borderPane, 800, 600);
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
        String SQL = "WITH \n" +
                "\n" +
                "selected_skills(ks_code) AS(\n" +
                "    SELECT ks_code\n" +
                "    FROM knowledge_skill\n" +
                "    WHERE ks_title IN(" + StringUtils.join(selectedSkills, ", ") + ")),\n" +
                "\n" +
                "qualified_jobs AS(\n" +
                "    SELECT j.job_code\n" +
                "    FROM job j\n" +
                "    WHERE NOT EXISTS ((SELECT ks_code\n" +
                "                        FROM required_skill rs\n" +
                "                        WHERE j.job_code = rs.job_code)\n" +
                "                        MINUS\n" +
                "                        (SELECT ks_code\n" +
                "                        FROM selected_skills))),\n" +
                "\n" +
                "q_jobs_desc AS(\n" +
                "    SELECT *\n" +
                "    FROM job NATURAL JOIN qualified_jobs\n" +
                ")\n" +
                "\n" +
                "SELECT DISTINCT job_code, cate_title, cate_description, comp_name, website\n" +
                "FROM q_jobs_desc NATURAL JOIN job_category NATURAL JOIN company NATURAL JOIN\n" +
                "has_category";

        try {
            c = DBConnect.connect(user, pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                String comp_name = rs.getString("comp_name");
                String website = rs.getString("website");
                String cate_title = rs.getString("cate_title");
                String cate_description = rs.getString("cate_description");
                VBox job = new VBox();
                Text comp_nameText = new Text(comp_name);
                comp_nameText.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
                Text websiteText = new Text(website);
                websiteText.setFont(Font.font("Tahoma", FontPosture.ITALIC, 10));
                Text cate_titleText = new Text(cate_title);
                Text cate_descriptionText = new Text(cate_description);
                job.getChildren().addAll(comp_nameText, websiteText, cate_titleText, cate_descriptionText);
                ObservableList<VBox> list = FXCollections.observableArrayList(job);
                jobList.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            new SelectScreen(user, pass, primaryStage);
        }
    }

}
