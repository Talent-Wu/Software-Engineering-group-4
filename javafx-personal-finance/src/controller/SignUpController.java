package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private Button confirmRegisterButton; // 注册按钮引用


    @FXML
    public void onConfirmRegisterAction(ActionEvent event) { // 注册按钮点击事件
        try {
            // 这里可以添加注册逻辑，例如验证输入、保存用户信息等
            // 假设注册成功，跳转到登录页面
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Stage currentStage = (Stage) confirmRegisterButton.getScene().getWindow(); // 通过按钮获取窗口

            // 保留窗口尺寸设置（可选）
            Scene newScene = new Scene(loginRoot, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            currentStage.setScene(newScene);
            currentStage.setTitle("用户登录");
        } catch (Exception e) {
            // 建议使用Alert替代Label显示错误
            showErrorAlert("注册失败: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("系统错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}