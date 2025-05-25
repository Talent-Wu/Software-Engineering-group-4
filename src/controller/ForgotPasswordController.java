package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.DataUtil;
import utils.Dialog;
import utils.StageContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ForgotPasswordController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button resetButton;

    @FXML
    private Button backButton;

    @FXML
    public void onResetPasswordAction() {
        String username = nameField.getText();
        String email = emailField.getText();

        // 检查用户名和邮箱是否匹配
        File file = new File("data/" + username);
        if (file.exists()) {
            // 这里添加发送邮件或更新密码的逻辑
            System.out.println("Resetting password for username: " + username + ", email: " + email);

            // TODO 要不要改成username
            User user = DataUtil.readProfile(username);
            DataUtil.saveProfile(username, user.getEmail(), newPasswordField.getText());
            Dialog.alert("Password reset to " + newPasswordField.getText());

            StageContainer.switchStage("login");
        } else {
            Dialog.alert("The account or email does not exist.");
        }
    }

    @FXML
    public void onBackAction() {
        StageContainer.switchStage("login");
    }

}
