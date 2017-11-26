import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.sql.*;

class AddEmployeeScreen {
    private Connection c;
    private String user, pass;
    private ComboBox<String> companyComboBox = new ComboBox<>();
    private String companyID;
    private TextField nameField = new TextField();
    private ComboBox<String> genderComboBox = new ComboBox<>();
    private NumberTextField streetNumberField = new NumberTextField();
    private TextField streetField = new TextField();
    private TextField aptField = new TextField();
    private TextField cityField = new TextField();
    private ComboBox<String> stateComboBox = new ComboBox<>();
    private NumberTextField zipCodeField = new NumberTextField();
    private TextField emailField = new TextField();
    private ComboBox<String> phoneComboBox = new ComboBox<>();
    private TextField phoneField = new TextField();
    private ComboBox<String> phoneComboBox2 = new ComboBox<>();
    private TextField phoneField2 = new TextField();
    private int perID;
    private TextField jobCodeField = new TextField();
    private ComboBox<String> employeeModeComboBox = new ComboBox<>();
    private NumberTextField payRateField = new NumberTextField();
    private ComboBox<String> payTypeComboBox = new ComboBox<>();
    private DatePicker startDateDatePicker = new DatePicker();

    AddEmployeeScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

        Label sceneTitle = new Label("Add Employee");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        gridPane.add(sceneTitle, 0, 0, 4, 1);

        Label chooseCompanyLabel = new Label("Choose company:");
        gridPane.add(chooseCompanyLabel, 0,1,1,1);
        companyComboBox.setOnAction(e -> getCompanyID());
        gridPane.add(companyComboBox, 1, 1, 3, 1);

        Label nameLabel = new Label("Name:");
        gridPane.add(nameLabel, 0, 2,1,1);

        gridPane.add(nameField, 1,2,1,1);

        Label genderLabel = new Label("Gender:");
        gridPane.add(genderLabel, 2, 2, 1, 1);

        genderComboBox.getItems().addAll("male", "female");
        gridPane.add(genderComboBox, 3, 2, 1, 1);

        Label streetNumberLabel = new Label("Street Number:");
        gridPane.add(streetNumberLabel, 0, 3,1,1);

        gridPane.add(streetNumberField, 1,3,1,1);

        Label streetLabel = new Label("Street Name:");
        gridPane.add(streetLabel, 2, 3,1,1);

        gridPane.add(streetField, 3,3,1,1);

        Label aptLabel = new Label("Apt Number:");
        gridPane.add(aptLabel, 2, 4,1,1);

        gridPane.add(aptField, 3,4,1,1);

        Label cityNumberLabel = new Label("City:");
        gridPane.add(cityNumberLabel, 0, 5,1,1);

        gridPane.add(cityField, 1,5,1,1);

        Label stateLabel = new Label("State:");
        gridPane.add(stateLabel, 2, 5,1,1);

        gridPane.add(stateComboBox, 3,5,1,1);
        stateComboBox.getItems().addAll("AK",
                "AL",
                "AR",
                "AS",
                "AZ",
                "CA",
                "CO",
                "CT",
                "DC",
                "DE",
                "FL",
                "GA",
                "GU",
                "HI",
                "IA",
                "ID",
                "IL",
                "IN",
                "KS",
                "KY",
                "LA",
                "MA",
                "MD",
                "ME",
                "MI",
                "MN",
                "MO",
                "MS",
                "MT",
                "NC",
                "ND",
                "NE",
                "NH",
                "NJ",
                "NM",
                "NV",
                "NY",
                "OH",
                "OK",
                "OR",
                "PA",
                "PR",
                "RI",
                "SC",
                "SD",
                "TN",
                "TX",
                "UT",
                "VA",
                "VI",
                "VT",
                "WA",
                "WI",
                "WV",
                "WY");
        stateComboBox.setValue("AK");

        Label zipCodeLabel = new Label("Zip Code");
        gridPane.add(zipCodeLabel, 0, 6,1,1);

        gridPane.add(zipCodeField, 1,6,1,1);

        Label emailLabel = new Label("Email:");
        gridPane.add(emailLabel, 2, 6,1,1);

        gridPane.add(emailField, 3,6,1,1);

        Label phoneLabel = new Label("Primary Phone:");
        gridPane.add(phoneLabel, 0, 7,1,1);

        phoneComboBox.getItems().addAll("cell", "home", "work", "other");
        phoneComboBox.setValue("cell");
        gridPane.add(phoneComboBox, 1, 7, 1, 1);

        gridPane.add(phoneField, 2,7,2,1);

        Label phoneLabel2 = new Label("Secondary Phone:");
        gridPane.add(phoneLabel2, 0, 8,1,1);

        phoneComboBox2.getItems().addAll("cell", "home", "work", "other");
        phoneComboBox2.setValue("home");
        gridPane.add(phoneComboBox2, 1, 8, 1, 1);

        gridPane.add(phoneField2, 2,8,2,1);

        Label startDateLabel = new Label("Start Date");
        gridPane.add(startDateLabel, 0,9,1,1);

        gridPane.add(startDateDatePicker, 1,9,1,1);

        Label employeeModeLabel = new Label("Mode:");
        gridPane.add(employeeModeLabel, 2, 9,1,1);

        employeeModeComboBox.getItems().addAll("full-time", "part-time");
        employeeModeComboBox.setValue("full-time");
        gridPane.add(employeeModeComboBox, 3, 9, 1, 1);

        Label payRateLabel = new Label("Pay Rate:");
        gridPane.add(payRateLabel, 0, 10,1,1);

        gridPane.add(payRateField, 1,10,1,1);

        Label payTypeLabel = new Label("Pay Type:");
        gridPane.add(payTypeLabel, 2, 10,1,1);

        payTypeComboBox.getItems().addAll("wage", "salary");
        payTypeComboBox.setValue("wage");
        gridPane.add(payTypeComboBox, 3,10,1,1);

        Label jobCodeLabel = new Label("Job Code:");
        gridPane.add(jobCodeLabel, 0,11,1,1);

        gridPane.add(jobCodeField, 1, 11, 1, 1);

        Button createEmployeeButton = new Button("Add Employee");
        createEmployeeButton.setOnAction(e -> addEmployee());
        gridPane.add(createEmployeeButton,1,12,2,2);

        // SET UP SCENE
        Scene scene = new Scene(gridPane, 640, 525);
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

    private void addEmployee() {
        Person employee = new Person(nameField.getText(), aptField.getText(), Integer.parseInt(streetNumberField.getText()),
                streetField.getText(), cityField.getText(), stateComboBox.getValue(),
                Integer.parseInt(zipCodeField.getText()), emailField.getText(), genderComboBox.getValue(), user, pass);
        if (employee.validateData()) {
            if (employee.createInDB()) {
                AlertBox.display("Success!", "Congratulations! You added a new employee!");
                perID = getPerID();
                // clearValues();
            } else {
                AlertBox.display("Error.", "Some sort of error occurred.");
            }
        } else {
            AlertBox.display("Invalid Information", "Please check that all information has been filled out correctly.");
        }
        if (!phoneField.getText().equals("")) {
            try {
                String SQL = "Insert into PHONE (PER_ID,PHONE,P_TYPE) values (?,?,?)";

                // c = DBConnect.connect(user,pass);

                PreparedStatement preparedStatement = c.prepareStatement(SQL);
                preparedStatement.setInt(1, perID);
                preparedStatement.setString(2, phoneField.getText());
                preparedStatement.setString(3, phoneComboBox.getValue());

                preparedStatement.execute();
            } catch (SQLException e) {
                AlertBox.display("Error running SQL!", "Some sort of error occurred while trying to add Phone number to database.");
                e.printStackTrace();
            }
        }
        if (!phoneField2.getText().equals("")) {
            try {
                String SQL = "Insert into PHONE (PER_ID,PHONE,P_TYPE) values (?,?,?)";

                PreparedStatement preparedStatement = c.prepareStatement(SQL);
                preparedStatement.setInt(1, perID);
                preparedStatement.setString(2, phoneField2.getText());
                preparedStatement.setString(3, phoneComboBox2.getValue());

                preparedStatement.execute();
            } catch (SQLException e) {
                AlertBox.display("Error running SQL!", "Some sort of error occurred while trying to add Phone number 2 to database.");
                e.printStackTrace();
            }
        }
        if ((!jobCodeField.getText().equals("")) && (!payRateField.getText().equals("")) &&
                (!startDateDatePicker.getValue().equals("")) && (!companyComboBox.getValue().equals(""))) {
            try {
                String SQL = "Insert into JOB (JOB_CODE, COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values (?,?,?,?,?)";

                PreparedStatement preparedStatement = c.prepareStatement(SQL);
                preparedStatement.setString(1, jobCodeField.getText());
                preparedStatement.setInt(2, Integer.parseInt(companyID));
                preparedStatement.setString(3, employeeModeComboBox.getValue());
                preparedStatement.setFloat(4, Float.parseFloat(payRateField.getText()));
                preparedStatement.setString(5, payTypeComboBox.getValue());

                preparedStatement.execute();

            } catch (SQLException e) {
                AlertBox.display("Error running SQL!", "Some sort of error occurred while trying to add to JOB table.");
                e.printStackTrace();
            }
            try {
                String SQL = "Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values (?,?,?,?)";
                PreparedStatement preparedStatement = c.prepareStatement(SQL);
                preparedStatement.setString(1, jobCodeField.getText());
                preparedStatement.setInt(2, perID);
                preparedStatement.setDate(3, Date.valueOf(startDateDatePicker.getValue()));
                preparedStatement.setDate(4, null);

                preparedStatement.execute();
            } catch (SQLException e) {
                AlertBox.display("Error running SQL!", "Some sort of error occurred while trying to add to WORKS table.");
                e.printStackTrace();
            }
        } else {
            System.out.println("The earth is invalid.");
        }
        clearValues();
    }

    private void clearValues() {
        companyComboBox.setValue("");
        nameField.setText("");
        aptField.setText("");
        streetNumberField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateComboBox.setValue("AK");
        zipCodeField.setText("");
        emailField.setText("");
        genderComboBox.setValue("");
        phoneComboBox.setValue("cell");
        phoneComboBox2.setValue("home");
        phoneField.setText("");
        phoneField2.setText("");
        jobCodeField.setText("");
        startDateDatePicker.setValue(null);
        employeeModeComboBox.setValue("full-time");
        payRateField.setText("");
        payTypeComboBox.setValue("wage");
    }

    private void getCompanyID() {
        String SQL = "select comp_id from company where comp_name='" + companyComboBox.getValue() + "'";
        companyID = "";
        try {
            c = DBConnect.connect(user,pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);
            if (rs.next())
                companyID = rs.getString("comp_id");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            AlertBox.display("Error!", "Error writing to database. Please check internet connection.");
        }
    }

    private int getPerID() {
        String SQL = "select per_id from person where per_name='" + nameField.getText() + "'";
        int returnID = 0;
        try {
            c = DBConnect.connect(user,pass);
            ResultSet rs = c.createStatement().executeQuery(SQL);
            if (rs.next())
                returnID =  rs.getInt("per_id");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            AlertBox.display("Error!", "Error writing to database. Please check internet connection.");
        }
        return returnID;
    }

    public class NumberTextField extends TextField
    {

        @Override
        public void replaceText(int start, int end, String text)
        {
            if (validate(text))
            {
                super.replaceText(start, end, text);
            }
        }

        @Override
        public void replaceSelection(String text)
        {
            if (validate(text))
            {
                super.replaceSelection(text);
            }
        }

        private boolean validate(String text)
        {
            return text.matches("[0-9]*");
        }
    }
}
