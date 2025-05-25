package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.StageContainer;

public class LoginController {

    @FXML
    public TextField name;
    @FXML
    public TextField email;
    @FXML
    public TextField password;
    @FXML
    public Label tip;

    public LoginController() {
    }

    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        String name = this.name.getText();
        String password = this.password.getText();
        StageContainer.switchStage("desktop");
    }

    public void onSignUp(MouseEvent mouseEvent) {
        StageContainer.switchStage("signup");
    }
}
