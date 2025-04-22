package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane; // 确保导入 GridPane
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.UserService;
import utils.StageContainer;

public class LoginController {

    @FXML
    private TextField name;

    @FXML
    private GridPane passwordGrid;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    public void onLogin() {
        System.out.println("Login button clicked.");

        // 获取用户输入
        String username = name.getText();
        String passwordInput = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();

        System.out.println("Username: " + username);
        System.out.println("Password: " + passwordInput);

        // 验证用户名和密码
        if (UserService.validateUser(username, passwordInput)) {
            // 登录成功，切换到主页面
            try {
                System.out.println("Attempting to load home.fxml...");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
                Parent root = loader.load();
                StageContainer.switchStage("desktop");
                System.out.println("home.fxml loaded successfully.");

                // 获取当前 Stage
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(root));
                System.out.println("Scene switched to home page.");
            } catch (Exception e) {
                System.out.println("Error switching to home page: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // 登录失败，弹出警告
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    public void onForgotPasswordAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ForgotPassword.fxml"));
            Parent forgotPasswordRoot = loader.load();
            Stage currentStage = (Stage) forgotPasswordLink.getScene().getWindow();

            javafx.scene.Scene newScene = new javafx.scene.Scene(forgotPasswordRoot, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            currentStage.setScene(newScene);
            currentStage.setTitle("Forgot Password");
        } catch (Exception e) {
            System.out.println("The forgot password page cannot be loaded: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onSignUp() {
        System.out.println("Sign Up link clicked.");
        try {
            System.out.println("Attempting to load SignUp.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SignUp.fxml"));
            Parent root = loader.load();
            System.out.println("SignUp.fxml loaded successfully.");

            Stage stage = (Stage) forgotPasswordLink.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("Scene switched to sign up page.");
        } catch (Exception e) {
            System.out.println("Error switching to sign up page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void togglePasswordVisibility() {
        ObservableList<Node> children = passwordGrid.getChildren();
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}