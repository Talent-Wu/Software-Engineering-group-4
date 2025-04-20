package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ForgotPasswordController {

    @FXML
    private TextField accountField;

    @FXML
    private TextField emailField;

    @FXML
    public void onResetPasswordAction() {
        String account = accountField.getText();
        String email = emailField.getText();

        // 校验账户格式：必须是含有英文的10位编码
        if (!account.matches(".*[a-zA-Z].*") || account.length() != 10) {
            showAlert("Invalid Account", "Account must be a 10-character code containing at least one English letter.");
            return;
        }

        // 校验邮箱格式：必须是5位以上十三位以下的数字+@+2-5位字母+.com
        if (!email.matches("\\d{5,13}@[a-zA-Z]{2,5}\\.com")) {
            showAlert("Invalid Email", "Email must be in the format: 5-13 digits + @ + 2-5 letters + .com");
            return;
        }

        // 这里添加发送邮件或更新密码的逻辑
        System.out.println("Resetting password for account: " + account + ", email: " + email);

        // 假设密码重置成功
        System.out.println("Password reset to 11111111.");

        // 跳转回主界面
        try {
            System.out.println("Attempting to load Main.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml")); // 假设主界面的 FXML 文件路径是 "/fxml/login.fxml"
            Parent root = loader.load();
            System.out.println("Main.fxml loaded successfully.");

            Stage stage = (Stage) accountField.getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("Scene switched to main page.");
        } catch (Exception e) {
            System.out.println("Error switching to main page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 弹出警告对话框
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}