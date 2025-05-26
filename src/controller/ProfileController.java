package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.User;
import utils.DataUtil;
import utils.Dialog;
import utils.StageContainer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class ProfileController {

    public Button disassociateButton;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private Label associatedAccountsLabel;
    @FXML
    private Button associatedAccountsInfo; // 修改为 Button 类型
    @FXML
    private VBox requestsContainer;
    @FXML
    private VBox mainContainer;
    @FXML
    public void initialize() {
        // 设置当前用户的邮箱和用户名到文本框中
        email.setText(DataUtil.currentUser.getEmail());
        username.setText(DataUtil.currentUser.getUsername());

        // 检查用户头像文件是否存在，如果存在则设置头像
        File file = new File("data/" + DataUtil.currentUser.getEmail() + "/avatar.png");
        if (file.exists()) {
            setAvatarImage(file);
        }

        // 加载关联账号信息
        loadAssociatedAccounts();
        // 加载待处理的关联请求
        loadAssociationRequests();
    }


    private void setAvatarImage(File file) {
        // 从文件路径创建图像对象，并设置到头像 ImageView 中
        Image image = new Image("file:" + file.getAbsolutePath());
        avatarImageView.setImage(image);
    }

    @FXML
    private void saveProfile() {
        // 获取用户输入的邮箱和新用户名
        String userEmail = email.getText();
        String newUsername = username.getText();

        // 保存用户资料
        DataUtil.saveProfile(newUsername, userEmail, DataUtil.currentUser.getPassword());

        // 更新当前用户的邮箱和用户名
        DataUtil.currentUser.setEmail(userEmail);
        DataUtil.currentUser.setUsername(newUsername);

        // 提示用户资料保存成功
        Dialog.alert("Profile saved successfully");

    }

    @FXML
    public void disassociateAccount() {
        // 提示用户输入要解除关联的账号用户名
        String associatedUsername = Dialog.input("Enter the username of the account you want to disassociate:");
        if (!associatedUsername.isEmpty()) {
            // 尝试解除关联
            if (DataUtil.disassociateAccount(DataUtil.currentUser.getUsername(), associatedUsername)) {
                // 提示关联解除成功，并重新加载关联账号信息和待处理请求
                Dialog.alert("Association with " + associatedUsername + " has been successfully disassociated.");
                loadAssociatedAccounts();
                loadAssociationRequests();
            } else {
                // 提示解除关联失败
                Dialog.alert("Failed to disassociate the account.");
            }
        } else {
            // 提示用户名不能为空
            Dialog.alert("Username cannot be empty.");
        }
    }

    public void uploadAvatar(MouseEvent mouseEvent) {
        // 获取当前窗口的 Stage 对象
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();

        // 创建文件选择器
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload");

        // 设置文件选择器的文件过滤条件为仅显示 PNG 图片
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png"));

        // 显示文件选择对话框
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            // 设置目标文件路径
            File targetFile = new File("data/" + DataUtil.currentUser.getUsername() + "/avatar.png");

            try {
                // 将选择的文件复制到目标文件路径
                Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // 设置新的头像图片
                setAvatarImage(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
                // 提示上传头像失败及错误信息
                Dialog.alert("Failed to upload avatar: " + e.getMessage());
            }
        }
    }

    // 退出到登录界面
    @FXML
    public void onExitAction() {
        // 切换到登录界面
        StageContainer.switchStage("login");
    }

    // 关联其他账号的方法
    @FXML
    public void associateOtherAccount() {
        // 提示用户输入要关联的账号用户名
        String associatedUsername = Dialog.input("Enter the username of the account you want to associate:");
        if (!associatedUsername.isEmpty()) {
            // 检查关联账号是否存在
            if (DataUtil.readProfile(associatedUsername) != null) {
                // 发送关联请求
                DataUtil.sendAssociationRequest(DataUtil.currentUser.getUsername(), associatedUsername);
                // 提示关联请求已发送
                Dialog.alert("Association request sent to " + associatedUsername);
            } else {
                // 提示账号不存在
                Dialog.alert("The account does not exist.");
            }
        } else {
            // 提示用户名不能为空
            Dialog.alert("Username cannot be empty.");
        }
    }

    // controller/ProfileController.java
    private void loadAssociatedAccounts() {
        // 获取当前用户的关联账号信息（键为旧用户名）
        Map<String, String> associatedAccounts = DataUtil.getAssociatedAccounts(DataUtil.currentUser.getUsername());

        if (!associatedAccounts.isEmpty()) {
            associatedAccountsLabel.setVisible(true);
            associatedAccountsInfo.setVisible(true);

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : associatedAccounts.entrySet()) {
                String oldUsername = entry.getKey();
                String permission = entry.getValue();

                // 读取关联账号的最新信息
                User associatedUser = DataUtil.readProfile(oldUsername);
                // 使用最新的用户名，如果读取失败则使用旧用户名
                String displayName = (associatedUser != null) ? associatedUser.getUsername() : oldUsername;

                sb.append(displayName).append(" (").append(permission).append(")\n");
            }
            associatedAccountsInfo.setText(sb.toString());
        } else {
            associatedAccountsLabel.setVisible(false);
            associatedAccountsInfo.setVisible(false);
        }
    }

    // 刷新关联账号信息
    public void refreshAssociatedAccounts() {
        // 重新加载关联账号信息
        loadAssociatedAccounts();
    }

    // 加载待处理的关联请求
    private void loadAssociationRequests() {
        if (requestsContainer != null) {
            // 清空请求容器中的内容
            requestsContainer.getChildren().clear();
            // 获取当前用户的待处理关联请求
            List<String[]> requests = DataUtil.readAssociationRequests(DataUtil.currentUser.getUsername());

            for (String[] request : requests) {
                if (request.length >= 1) {
                    // 获取请求者用户名和时间戳
                    String requester = request[0];
                    String timestamp = request.length > 1 ? request[1] : "";

                    // 创建请求框
                    HBox requestBox = new HBox(10);
                    requestBox.setPadding(new javafx.geometry.Insets(5));

                    // 创建请求者用户名标签
                    Label usernameLabel = new Label(requester);
                    usernameLabel.setPrefWidth(150);

                    // 创建接受按钮
                    Button acceptButton = new Button("Accept");
                    acceptButton.setOnAction(e -> acceptRequest(requester));

                    // 创建拒绝按钮
                    Button rejectButton = new Button("Reject");
                    rejectButton.setOnAction(e -> rejectRequest(requester));

                    // 将标签和按钮添加到请求框中
                    requestBox.getChildren().addAll(usernameLabel, acceptButton, rejectButton);
                    // 将请求框添加到请求容器中
                    requestsContainer.getChildren().add(requestBox);
                }
            }
        }
    }

    // 接受关联请求
    private void acceptRequest(String requester) {
        // 尝试接受关联请求
        if (DataUtil.acceptAssociationRequest(requester, DataUtil.currentUser.getUsername())) {
            // 提示关联已接受，并重新加载待处理请求和关联账号信息
            Dialog.alert("Association with " + requester + " accepted.");
            loadAssociationRequests();
            loadAssociatedAccounts();
        } else {
            // 提示接受请求失败
            Dialog.alert("Failed to accept request.");
        }
    }

    // 拒绝关联请求
    private void rejectRequest(String requester) {
        // 拒绝关联请求，并重新加载待处理请求
        DataUtil.rejectAssociationRequest(requester, DataUtil.currentUser.getUsername());
        loadAssociationRequests();
    }

    // 更新关联账号信息
    public void updateAssociatedAccountsInfo(String username) {
        // 获取关联账号信息
        Map<String, String> associatedAccounts = DataUtil.getAssociatedAccounts(username);

        // 更新显示信息
        StringBuilder info = new StringBuilder();
        for (Map.Entry<String, String> entry : associatedAccounts.entrySet()) {
            info.append(entry.getKey()).append(" (").append(entry.getValue()).append(")\n");
        }

    }

    @FXML
    public void viewAssociatedAccountFuture() {
        // 提示用户输入要查看的关联账号用户名
        String associatedUsername = Dialog.input("Enter the username of the associated account you want to view:");
        if (!associatedUsername.isEmpty()) {
            // 检查输入的账号是否为关联账号
            Map<String, String> associatedAccounts = DataUtil.getAssociatedAccounts(DataUtil.currentUser.getUsername());
            if (associatedAccounts.containsKey(associatedUsername)) {
                // 设置正在查看的用户信息
                DataUtil.viewingUser = DataUtil.readProfile(associatedUsername);
                if (DataUtil.viewingUser != null) {
                    // 切换到对方账户的 future 页面
                    DataUtil.tempUser=DataUtil.currentUser;
                    DataUtil.currentUser= DataUtil.viewingUser;
                    StageContainer.switchStage("future");
                } else {
                    // 提示关联账号不存在
                    Dialog.alert("The associated account does not exist.");
                }
            } else {
                // 提示输入的账号不是关联账号
                Dialog.alert("The entered account is not an associated account.");
            }
        } else {
            // 提示用户名不能为空
            Dialog.alert("Username cannot be empty.");
        }
    }
    @FXML
    public void switchBackground() {
        ObservableList<String> styleClasses = mainContainer.getStyleClass();

        // 移除当前背景样式
        boolean hasBg4Img = styleClasses.remove("bg4-img");
        boolean hasBg5Img = styleClasses.remove("bg5-img");

        // 根据移除的状态决定下一个背景
        if (hasBg4Img) {
            styleClasses.add("bg5-img");
        } else {
            styleClasses.add("bg4-img");
        }
    }
}