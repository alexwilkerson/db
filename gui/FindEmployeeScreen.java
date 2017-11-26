import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class FindEmployeeScreen {
    private Stage primaryStage;
    private String user, pass;
    private Connection c;
    private ComboBox<String> jobCodeComboBox = new ComboBox<>();
    private ListView<VBox> employeeList = new ListView<>();
    private ListView<String> skillsRequired = new ListView<>();

    FindEmployeeScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;
        this.primaryStage = primaryStage;

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));

        HBox bottomMenu = new HBox(10);
        Label chooseJobCodeLabel = new Label("Select a job code to find suitable candidates:");
        jobCodeComboBox.setOnAction(e -> {
            refreshSkillsRequiredList();
            findEmployees();
        });
        bottomMenu.getChildren().addAll(chooseJobCodeLabel, jobCodeComboBox);
        bottomMenu.setAlignment(Pos.CENTER);
        borderPane.setBottom(bottomMenu);
        BorderPane.setMargin(bottomMenu, new Insets(10, 0 ,0 ,0));

        VBox rightMenu = new VBox(10);
        Label employeeListLabel = new Label("Candidate Employees:");
        rightMenu.getChildren().addAll(employeeListLabel, employeeList);
        borderPane.setRight(rightMenu);

        VBox leftMenu = new VBox(10);
        Label skillsRequiredLabel = new Label("Skills Required:");
        leftMenu.getChildren().addAll(skillsRequiredLabel, skillsRequired);
        borderPane.setLeft(leftMenu);
        BorderPane.setMargin(leftMenu, new Insets(0, 10, 0 ,0));

        Scene scene = new Scene(borderPane, 525, 400);
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

        String SQL = "SELECT job_code FROM job";
        try {
            c = DBConnect.connect(user, pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                String current = rs.getString("job_code");
                ObservableList<String> list = FXCollections.observableArrayList(current);
                jobCodeComboBox.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            new SelectScreen(user, pass, primaryStage);
        }

    }

    private void findEmployees() {
        employeeList.getItems().clear();
        String SQL = "SELECT per_name, street_number, street_name, city, state, zip_code, email\n" +
                "FROM person p\n" +
                "WHERE NOT EXISTS (\n" +
                "    (SELECT ks_code\n" +
                "    FROM required_skill\n" +
                "    WHERE job_code = '" + jobCodeComboBox.getValue() + "')\n" +
                "    MINUS\n" +
                "    (SELECT ks_code\n" +
                "    FROM has_skill hs\n" +
                "    WHERE hs.per_id = p.per_id))";
        try {
            c = DBConnect.connect(user, pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                String perName = rs.getString("per_name");
                String streetNumber = rs.getString("street_number");
                String streetName = rs.getString("street_name");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip_code");
                String email = rs.getString("email");
                VBox job = new VBox();
                Text perNameText = new Text(perName);
                perNameText.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
                Hyperlink emailLink = new Hyperlink(email);
                emailLink.setFont(Font.font("Tahoma", FontPosture.ITALIC, 10));
                Text addressText = new Text(streetNumber + " " + streetName + "\n" +
                city + ", " + state + " " + zip);
                addressText.setFont(Font.font("Tahoma", 11));
                job.getChildren().addAll(perNameText, emailLink, addressText);
                ObservableList<VBox> list = FXCollections.observableArrayList(job);
                employeeList.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            new SelectScreen(user, pass, primaryStage);
        }
    }

    private void refreshSkillsRequiredList() {
        skillsRequired.getItems().clear();
        String SQL = "SELECT ks_title FROM\n" +
                "job NATURAL JOIN required_skill NATURAL JOIN knowledge_skill\n" +
                "WHERE job_code = '" + jobCodeComboBox.getValue() + "'";
        try {
            c = DBConnect.connect(user, pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                String knowledgeSkill = rs.getString("ks_title");
                ObservableList<String> list = FXCollections.observableArrayList(knowledgeSkill);
                skillsRequired.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            new SelectScreen(user, pass, primaryStage);
        }
    }
}
