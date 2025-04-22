package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Dialog {
    public static void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("System Message");
        alert.setHeaderText(message);
        alert.setContentText("");
        alert.showAndWait();
    }

    public static String input(String message){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dialog");
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog.showAndWait().orElse("");
    }
}
