import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;

class QueriesScreen {
    // TABLE VIEW AND DATA
    private ObservableList<ObservableList<String>> data;
    private TableView<ObservableList<String>> tableView = new TableView<>();
    private TextArea sqlInput = new TextArea();
    private Label queryLabel = new Label();
    private String user, pass;

    QueriesScreen(String user, String pass, Stage primaryStage) {
        this.user = user;
        this.pass = pass;

        VBox topMenu = new VBox(10);

        HBox querySelectionLine = new HBox(10);
        ComboBox<Queries> queriesComboBox = new ComboBox<>();
//        queriesComboBox.getItems().addAll(
//                "Query 1",
//                "Query 2",
//                "Query 3"
//        );
        queriesComboBox.getItems().setAll(Queries.values());
        queriesComboBox.setValue(Queries.Q1);
        queriesComboBox.setOnAction(e -> runQueryFromComboBox(queriesComboBox.getValue()));

        querySelectionLine.setAlignment(Pos.CENTER_LEFT);
        querySelectionLine.getChildren().addAll(queriesComboBox, queryLabel);

        Button switchSQL = new Button("Run Query");
        switchSQL.setOnAction(e -> buildData(sqlInput.getText()));
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

        runQueryFromComboBox(queriesComboBox.getValue());

        Scene scene = new Scene(borderPane, 1200, 600);
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

    }

    // CONNECTION DATABASE
    private void buildData(String SQL) {
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

            // Table column added dynamically
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                // we are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>) param -> {
                    if (param.getValue().get(j) != null) {
                        return new SimpleStringProperty(param.getValue().get(j));
                    } else {
                        return new SimpleStringProperty();
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            // Data added to ObservableList
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

    private void runQueryFromComboBox(Queries query) {
        queryLabel.setText(query.getDesc());
        sqlInput.setText(query.getSql());
        buildData(sqlInput.getText());
    }

}
