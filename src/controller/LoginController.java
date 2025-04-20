package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    public TextField usernameTextField;
    @FXML
    public Label tip;

    public LoginController() {
    }

    @FXML
    public void onLoginActon(MouseEvent mouseEvent) {
        String username = usernameTextField.getText();

    }
}
