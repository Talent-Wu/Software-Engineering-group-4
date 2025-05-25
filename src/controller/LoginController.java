package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import utils.DataUtil;
import utils.Dialog;
import utils.StageContainer;

public class LoginController {

    public TextField username;
    public CheckBox showPasswordCheckBox;
    public PasswordField passwordField;
    public TextField passwordTextField;


    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        String name = this.username.getText();
        String password = this.passwordField.getText();
        if(showPasswordCheckBox.isSelected()) {
            password = this.passwordTextField.getText();
        }

        User user = DataUtil.readProfile(name);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                DataUtil.currentUser = user;
                StageContainer.switchStage("layout");
            } else {
                Dialog.alert("Password is wrong!");
            }
        } else {
            Dialog.alert("Username is wrong!");
        }
    }

    public void onSignUp(MouseEvent mouseEvent) {
        StageContainer.switchStage("signup");
    }

    public void togglePasswordVisibility(ActionEvent actionEvent) {
        if (showPasswordCheckBox.isSelected()) {
            // 显示密码
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setPromptText(passwordField.getPromptText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            // 隐藏密码
            passwordField.setText(passwordTextField.getText());
            passwordField.setPromptText(passwordTextField.getPromptText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
        }
    }

    public void onForgotPasswordAction(ActionEvent actionEvent) {
        StageContainer.switchStage("forgotPassword");
    }
}
