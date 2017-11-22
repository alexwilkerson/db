package jobby;

import java.io.IOException;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    public Label accessLabel;
    public String sessionName;
    public String sessionUser;
    public String sessionPassword;

    @FXML
    public TextArea responseField;
    @FXML
    public TextArea queryField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField userField;
    @FXML
    private String response = "";

    @FXML
    public void queryButton(ActionEvent actionEvent) {
        runQuery(queryField.getText());
    }

    public boolean runQuery(String q) {
        System.out.println(sessionUser + " " + sessionPassword);
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
                    "jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl", sessionUser, sessionPassword);
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

    public void toPasswordInput(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            passwordField.requestFocus();
        }
    }

    public void authenticate(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if ((userField.getText().equals("awilkers")) && (passwordField.getText().equals("zT7RXLfP"))) {
                accessGranted("Alex", "awilkers", "zT7RXLfP");

                System.out.println("Welcome, Alex.");
            } else if ((userField.getText().equals("kbongcas")) && (passwordField.getText().equals("9TTtPT97"))) {
                accessGranted("Kevin", "kbongcas", "9TTtPT97");
                System.out.println("Welcome, Kevin.");
            } else{
                accessLabel.setText("Access Denied!");
                passwordField.setText("");
                passwordField.requestFocus();
            }
        }
    }

    public void accessGranted(String name, String uname, String pword) {
        sessionName = name;
        sessionUser = uname;
        sessionPassword = pword;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Welcome, " + sessionName + "!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}