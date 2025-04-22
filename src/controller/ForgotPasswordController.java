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
import model.UserService;

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
        String name = nameField.getText();
        String email = emailField.getText();

        // 校验账户格式：必须是12位以下全英文的内容，首字母必须大写
        if (!name.matches("^[A-Z][a-zA-Z]{0,11}$")) {
            showAlert("Invalid Account", "Account must be a 12-character or less code starting with a capital letter and containing only English letters.");
            return;
        }

        // 校验邮箱格式：必须是5位以上十三位以下的数字+@
        if (!email.matches("^[a-zA-Z0-9]{5,13}@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$")) {
            showAlert("Invalid Email", "Email must be in the format: 5-13 digits + @ + 2-5 letters + .com");
            return;
        }

        // 检查用户名和邮箱是否匹配
        User user = UserService.findUserByUsername(name);
        if (user == null || !user.getEmail().equals(email)) {
            showAlert("User Not Found", "The account or email does not exist.");
            return;
        }

        // 这里添加发送邮件或更新密码的逻辑
        System.out.println("Resetting password for account: " + name + ", email: " + email);

        // 假设密码重置成功
        System.out.println("Password reset to 11111111.");

        // 跳转回主界面
        try {
            System.out.println("Attempting to load login.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml")); // 假设登录界面的 FXML 文件路径是 "/fxml/login.fxml"
            Parent root = loader.load();
            System.out.println("login.fxml loaded successfully.");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("Scene switched to login page.");
        } catch (Exception e) {
            System.out.println("Error switching to login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackAction() {
        System.out.println("Back button clicked.");

        try {
            System.out.println("Attempting to load login.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml")); // 假设登录界面的 FXML 文件路径是 "/fxml/login.fxml"
            Parent root = loader.load();
            System.out.println("login.fxml loaded successfully.");

            Stage stage = (Stage) nameField.getScene().getWindow();
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