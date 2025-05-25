package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.DataUtil;
import utils.Dialog;
import utils.StageContainer;

public class SignUpController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void onCreateAccount(MouseEvent mouseEvent) {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            Dialog.alert("Passwords do not match.");
            return;
        }

        if (!username.matches("^[A-Z][a-zA-Z]{0,12}$")) {
            Dialog.alert("Username must be up to 13 characters long, all English letters, and start with a capital letter.");
            return;
        }

        // 校验邮箱格式：必须是5位以上十三位以下的数字+@+2-5位字母+.com
        if (!email.matches("^[a-zA-Z0-9]{5,13}@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$")) {
            Dialog.alert("Email must be in the format: 5-13 digits + @ + 2-5 letters + .com");
            return;
        }

        if (DataUtil.createUserAndFiles(username, email, password)) {
            Dialog.alert("Sign up successfully");
            usernameTextField.clear();
            emailTextField.clear();
            passwordField.clear();
            StageContainer.switchStage("login");
        } else {
            Dialog.alert("Email has exists");
        }
    }

    public void onBackAction(ActionEvent actionEvent) {
        StageContainer.switchStage("login");
    }
}
