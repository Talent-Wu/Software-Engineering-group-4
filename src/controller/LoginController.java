package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.StageContainer;

import java.util.Objects;

public class LoginController {

    @FXML
    public TextField name;
    @FXML
    public TextField email;
    @FXML
    public TextField password;
    @FXML
    public Label tip;

    @FXML
    private Hyperlink forgotPasswordLink; // 引用FXML中的Forgot Password链接


    public LoginController() {
    }
    @FXML
    public void onForgotPasswordAction() { // 处理Forgot Password链接的点击事件
        try {
            Parent forgotPasswordRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/ForgotPassword.fxml")));
            Stage currentStage = (Stage) forgotPasswordLink.getScene().getWindow(); // 通过链接获取窗口

            Scene newScene = new Scene(forgotPasswordRoot, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            currentStage.setScene(newScene);
            currentStage.setTitle("忘记密码");
        } catch (Exception e) {
            System.out.println("无法加载忘记密码页面: " + e.getMessage());
        }
    }
    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        String name = this.name.getText();
        String password = this.password.getText();
        StageContainer.switchStage("home");
    }

    public void onSignUp(MouseEvent mouseEvent) {
        StageContainer.switchStage("signup");
    }
}
