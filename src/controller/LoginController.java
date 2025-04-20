package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Hyperlink signUpLink; // 引用FXML中的Sign Up链接
    @FXML
    private Hyperlink forgotPasswordLink; // 引用FXML中的Forgot Password链接

    @FXML
    public void onSignUpAction() { // 处理Sign Up链接的点击事件
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
            Stage currentStage = (Stage) signUpLink.getScene().getWindow(); // 通过链接获取窗口

            Scene newScene = new Scene(signUpRoot, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            currentStage.setScene(newScene);
            currentStage.setTitle("用户注册");
        } catch (Exception e) {
            System.out.println("无法加载注册页面: " + e.getMessage());
        }
    }

    @FXML
    public void onForgotPasswordAction() { // 处理Forgot Password链接的点击事件
        try {
            Parent forgotPasswordRoot = FXMLLoader.load(getClass().getResource("/fxml/ForgotPassword.fxml"));
            Stage currentStage = (Stage) forgotPasswordLink.getScene().getWindow(); // 通过链接获取窗口

            Scene newScene = new Scene(forgotPasswordRoot, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            currentStage.setScene(newScene);
            currentStage.setTitle("忘记密码");
        } catch (Exception e) {
            System.out.println("无法加载忘记密码页面: " + e.getMessage());
        }
    }
}