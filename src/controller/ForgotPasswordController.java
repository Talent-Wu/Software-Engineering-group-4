package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML
    private TextField accountField;

    @FXML
    private TextField emailField;

    @FXML
    public void onResetPasswordAction() {
        String account = accountField.getText();
        String email = emailField.getText();
        // 这里添加发送邮件或更新密码的逻辑
        System.out.println("Resetting password for account: " + account + ", email: " + email);
        // 假设密码重置成功
        System.out.println("Password reset to 11111111.");
    }
}