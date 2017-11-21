package jobby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Controller {

    @FXML
    private TextField urlField;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @FXML
    public void countWords(ActionEvent actionEvent) {
        try {
            URL url = new URL(urlField.getText());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        String line;
                        while((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (MalformedURLException murle) {
            throw new RuntimeException(murle);
        }
        System.out.println(urlField.getText());
    }
}