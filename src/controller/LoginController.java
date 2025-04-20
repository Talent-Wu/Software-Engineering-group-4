package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class LoginController {

    @FXML
    public TextField usernameTextField;
    @FXML
    public Label tip;

    @FXML
    public Hyperlink signUpLink; // 引用FXML中的Sign Up链接

    @FXML
    public void onLoginAction(MouseEvent mouseEvent) {
        String username = usernameTextField.getText();
        // 在这里添加登录逻辑
    }

    @FXML
    public void onSignUpAction(ActionEvent event) { // 处理Sign Up链接的点击事件
        try {
            // 加载注册页面的正确路径（假设SignUp.fxml在views目录）
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
            Stage currentStage = (Stage) signUpLink.getScene().getWindow(); // 通过链接获取窗口

            // 保留窗口尺寸设置（可选）
            Scene newScene = new Scene(signUpRoot, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            currentStage.setScene(newScene);
            currentStage.setTitle("用户注册");
        } catch (Exception e) {
            // 显示错误信息
            System.out.println("无法加载注册页面: " + e.getMessage());
        }
    }
}

    public LoginController() {
    }

    @FXML
    public void onLoginActon(MouseEvent mouseEvent) {
        String username = usernameTextField.getText();

    }
}
