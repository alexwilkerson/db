package jobby;

import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;

public class Controller {

    @FXML
    public TextArea responseField;
    public TextArea queryField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField userField;
    private String response = "";

    @FXML
    private TextField urlField;

    @FXML
    public void queryButton(ActionEvent actionEvent) {
        runQuery(queryField.getText());
    }

    public boolean runQuery(String q) {
        System.out.println(userField.getText());
        if (q.trim().charAt(q.length() - 1) == ';') {
            q = q.trim().substring(0, q.length() - 1);
        }
        System.out.println();
        System.out.println("implement this");
        System.out.println();
        System.out.println(q + ";");
        System.out.println();
        try{
            // step 1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // step 2 create the connection object
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl", userField.getText(), passwordField.getText());
            // step 3 create the statement object
            Statement stmt = con.createStatement();
            // step 4 execute query
            ResultSet rs = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                System.out.print(String.format("%-30s", rsmd.getColumnName(i)));
                response += String.format("%-30s", rsmd.getColumnName(i));
            }
            response += "\n";
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String colValue = rs.getString(i);
                    response += String.format("%-30s", colValue);
                    System.out.print(String.format("%-30s", colValue));
                }
                response += "\n";
                System.out.println();

            }
            responseField.setText(response);
            response = "";
            // step 5 close the connection object
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println();
            response = "";
            return false;
        }
        System.out.println();
        return true;
    }

}