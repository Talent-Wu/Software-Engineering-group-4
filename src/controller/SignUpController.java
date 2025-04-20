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
    private Button registerButton;

    @FXML
    public void onRegisterAction() {
        System.out.println("Register button clicked.");

        // 获取用户输入
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Confirm Password: " + confirmPassword);

        // 校验密码是否匹配
        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        // 校验账户格式：必须是12位以下全英文的内容，首字母必须大写
        if (!username.matches("^[A-Z][a-zA-Z]{0,11}$")) {
            showAlert("Invalid Username", "Username must be up to 12 characters long, all English letters, and start with a capital letter.");
            return;
        }

        // 校验邮箱格式：必须是5位以上十三位以下的数字+@+2-5位字母+.com
        if (!email.matches("\\d{5,13}@[a-zA-Z]{2,5}\\.com")) {
            showAlert("Invalid Email", "Email must be in the format: 5-13 digits + @ + 2-5 letters + .com");
            return;
        }

        // 假设注册成功，跳转到登录页面
        System.out.println("Registering user: " + username);
        // 这里可以添加更多的注册逻辑，例如保存用户信息等

        try {
            System.out.println("Attempting to load Login.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml")); // 假设登录界面的 FXML 文件路径是 "/fxml/Login.fxml"
            Parent root = loader.load();
            System.out.println("Login.fxml loaded successfully.");

            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("Scene switched to login page.");
        } catch (Exception e) {
            System.out.println("Error switching to login page: " + e.getMessage());
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