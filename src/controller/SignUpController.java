package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.StageContainer;

public class SignUpController {

    @FXML
    public TextField name;
    @FXML
    public TextField email;
    @FXML
    public PasswordField password;

    public SignUpController() {
    }

    @FXML
    public void onCreateAccount(MouseEvent mouseEvent) {
        System.out.println("dddddddddd");
        StageContainer.switchStage("login");
    }


    @FXML
    private void toLogin(MouseEvent event) {
        StageContainer.switchStage("login");
    }
}
