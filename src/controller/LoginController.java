package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.User;
import utils.DataUtil;
import utils.Dialog;
import utils.StageContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginController {

    public TextField username;
    public CheckBox showPasswordCheckBox;
    public PasswordField passwordField;
    public TextField passwordTextField;

    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        String name = this.username.getText();
        String password = this.passwordField.getText();
        if (showPasswordCheckBox.isSelected()) {
            password = this.passwordTextField.getText();
        }

        User user = DataUtil.readProfile(name);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                DataUtil.currentUser = user;

                // 加载关联账户信息
                DataUtil.loadAssociatedAccounts(DataUtil.currentUser);

                StageContainer.switchStage("layout");

                // 检查是否有新的关联请求
                List<String[]> requests = DataUtil.readAssociationRequests(name);
                // 过滤掉已经关联的请求
                List<String[]> unhandledRequests = new ArrayList<>();
                Map<String, String> associatedAccounts = DataUtil.getAssociatedAccounts(name);
                for (String[] request : requests) {
                    if (!associatedAccounts.containsKey(request[0])) {
                        unhandledRequests.add(request);
                    }
                }
                if (!unhandledRequests.isEmpty()) {
                    if (Dialog.confirm("You have new association requests. Do you want to check them now?")) {
                        // 处理未处理的请求
                        for (String[] request : unhandledRequests) {
                            String requester = request[0];
                            if (DataUtil.acceptAssociationRequest(requester, DataUtil.currentUser.getUsername())) {
                                // 设置默认权限
                                DataUtil.setAssociationPermission(DataUtil.currentUser.getUsername(), requester, "部分查看");
                                DataUtil.setAssociationPermission(requester, DataUtil.currentUser.getUsername(), "部分查看");
                            }
                        }
                        // 重新加载关联账号信息
                        ((ProfileController) StageContainer.getController("profile")).refreshAssociatedAccounts();
                    }
                }
            } else {
                Dialog.alert("Password is wrong!");
            }
        } else {
            Dialog.alert("Username is wrong!");
        }
    }

    public void onSignUp(MouseEvent mouseEvent) {
        StageContainer.switchStage("signup");
    }

    public void togglePasswordVisibility(ActionEvent actionEvent) {
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

    public void onForgotPasswordAction(ActionEvent actionEvent) {
        StageContainer.switchStage("forgotPassword");
    }
}