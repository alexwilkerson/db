import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;

public class Main extends Application {

    // TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    private TableView tableView;
    private TextArea sqlInput = new TextArea();
    private Label queryLabel = new Label();
    private String user, pass;

    // MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    // CONNECTION DATABASE
    public void buildData(String SQL) {
        SQL = SQL.trim();
        if (SQL.length() > 0) {
            if (SQL.charAt(SQL.length() - 1) == ';') {
                SQL = SQL.substring(0, SQL.length() - 1);
            }
        }
        queryLabel.setTextFill(Color.web("#000000"));
        tableView.getItems().clear();
        tableView.getColumns().clear();
        Connection c;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect(user, pass);
            // SQL FOR SELECTING
            // String SQL = "SELECT * FROM person";
            // ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**********************************
             * Table column added dynamically *
             **********************************/
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                // we are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> param) {
                        if (param.getValue().get(j) != null) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        } else {
                            return new SimpleStringProperty();
                        }
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while (rs.next()) {
                // Iterate row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    // Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row added " + row);
                data.add(row);
            }

            // FINALLY ADDED TO TableView
            tableView.setItems(data);

        } catch (SQLSyntaxErrorException ssee) {
            tableView.getItems().clear();
            tableView.getColumns().clear();
            queryLabel.setTextFill(Color.web("#FF0000"));
            queryLabel.setText("SQL Syntax Error!");
            // AlertBox.display("SQL Syntax Error!", "An error occurred in your SQL Syntax.");
            ssee.printStackTrace();
            System.out.println("SQL Syntax error.");
        } catch (Exception e) {
            tableView.getItems().clear();
            tableView.getColumns().clear();
            e.printStackTrace();
            AlertBox.display("Error Connecting", "Error Connecting to database. Please check internet connection.");
            System.out.println("Error on building data.");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        /*********************
         * SQL Query scene   *
         *********************/
        // TableView
        tableView = new TableView();

        // buildData("SELECT * FROM person");

        VBox topMenu = new VBox(10);

        HBox querySelectionLine = new HBox(10);
        ComboBox queriesComboBox = new ComboBox();
        queriesComboBox.getItems().addAll(
                "Query 1",
                "Query 2",
                "Query 3"
        );
        queriesComboBox.setValue("Query 1");
        queriesComboBox.setOnAction(e -> {
            runQueryFromComboBox(queriesComboBox.getValue().toString());
        });

        querySelectionLine.setAlignment(Pos.CENTER_LEFT);
        querySelectionLine.getChildren().addAll(queriesComboBox, queryLabel);

        Button switchSQL = new Button("Run Query");
        topMenu.getChildren().addAll(querySelectionLine, sqlInput, switchSQL);

        // buildData(sqlInput.getText());

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(scrollPane);
        BorderPane.setMargin(topMenu, new Insets(12,12,0,12));
        BorderPane.setMargin(scrollPane, new Insets(12,12,12,12));

        Scene scene = new Scene(borderPane, 1200, 600);

        /********************
         * Login scene      *
         ********************/

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
                    switchSQL.setOnAction(event -> {
                        queryLabel.setText("");
                    });
                    stage.setScene(scene);
                    stage.show();
                    runQueryFromComboBox("Query 1");
                } else if (userTextField.getText().trim().equals("kbongcas") && pwBox.getText().equals("9TTtPT97")) {
                    user = "kbongcas";
                    pass = "9TTtPT97";
                    switchSQL.setOnAction(event -> {
                        queryLabel.setText("");
                    });
                    stage.setScene(scene);
                    stage.show();
                    runQueryFromComboBox("Query 1");
                } else {
                    AlertBox.display("Wrong Credentials", "Username or Password incorrect.");
                    pwBox.setText("");
                }
            }
        });
        gridPane.add(pwBox, 1, 2);

        stage.setTitle("Pending Matters Job Corp");
        stage.setScene(new Scene(gridPane, 1200, 600));
        stage.show();

    }

    private void runQueryFromComboBox(String query) {
        switch (query) {
            case "Query 1":
                queryLabel.setText("List a company's workers by names.");
                sqlInput.setText("select per_name\n" +
                        "from person natural join works natural join job\n" +
                        "where comp_id = '8' and end_date is null;");
                break;
            case "Query 2":
                queryLabel.setText("List a company's staff by salary in descending order.");
                sqlInput.setText("select per_name, pay_rate\n" +
                        "from person natural join works natural join job\n" +
                        "where comp_id = '8' and pay_type = 'salary'\n" +
                        "order by pay_rate desc;");
                break;
            case "Query 3":
                queryLabel.setText("List company's labor cost (total salaries and wage rates by 1920 hours) in descending order.");
                sqlInput.setText("select comp_id, sum(case when pay_type = 'salary'\n" +
                        "    then pay_rate  else pay_rate*1920 end) as total_labor_cost\n" +
                        "\n" +
                        "from job natural join works\n" +
                        "group by comp_id\n" +
                        "order by total_labor_cost desc;");
                break;
            default:
                break;
        }
        buildData(sqlInput.getText());
    }
}
