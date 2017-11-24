import javafx.scene.input.KeyCode;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

class AlertBox {

    static void display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);
        label.setPadding(new Insets(10,10,10,10));
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> window.close());
        closeButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
