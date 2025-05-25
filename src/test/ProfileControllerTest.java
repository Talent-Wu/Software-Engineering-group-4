package test;

import controller.ProfileController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import utils.DataUtil;
import utils.Dialog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class ProfileControllerTest {

    private ProfileController controller;
    private TextField emailTextField;
    private TextField usernameTextField;
    private ImageView avatarImageView;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();

        // 获取FXML中的控件
        emailTextField = (TextField) root.lookup("#email");
        usernameTextField = (TextField) root.lookup("#username");
        avatarImageView = (ImageView) root.lookup("#avatarImageView");
    }

    @BeforeEach
    void setUp() {
        // 清除所有mock的静态方法调用
        Mockito.reset();
        // 初始化当前用户
        DataUtil.currentUser = new User("testuser@gmail.com", "123", "Johns");
        // 清理可能存在的测试文件
        File walletFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wallet.csv");
        File wishlistFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wishlist.csv");
        File profileFile = new File("data/" + DataUtil.currentUser.getUsername() + "/profile.csv");
        File avatarFile = new File("data/" + DataUtil.currentUser.getEmail() + "/avatar.png");
        walletFile.delete();
        wishlistFile.delete();
        profileFile.delete();
        avatarFile.delete();
    }

    @Test
    void testInitialize_ShouldSetUserInfoAndAvatar() throws InterruptedException {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            // 模拟当前用户
            User user = new User("testuser@gmail.com", "123", "Johns");

            // 模拟头像文件存在
            Path avatarPath = Paths.get("data/" + user.getEmail() + "/avatar.png");
            try {
                Files.createDirectories(avatarPath.getParent());
                Files.createFile(avatarPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 在 JavaFX 应用程序线程上执行初始化
            Platform.runLater(() -> {
                controller.initialize();

                // 验证用户信息是否正确设置
                assertEquals(user.getEmail(), emailTextField.getText());
                assertEquals(user.getUsername(), usernameTextField.getText());

                // 验证头像是否设置
                assertNotNull(avatarImageView.getImage());
            });

            // 等待 JavaFX 应用程序线程执行完毕
            Thread.sleep(1000);
        }
    }

    @Test
    void testSaveProfile_ShouldUpdateUserInfo() throws Exception {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class);
             MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
            // 设置新的邮箱
            String newEmail = "newtestuser@gmail.com";
            // 在 JavaFX 应用程序线程上设置文本
            Platform.runLater(() -> emailTextField.setText(newEmail));

            // 等待 JavaFX 应用程序线程执行完毕
            Thread.sleep(1000);

            // 使用反射调用私有方法
            Method saveProfileMethod = ProfileController.class.getDeclaredMethod("saveProfile");
            saveProfileMethod.setAccessible(true);
            saveProfileMethod.invoke(controller);

            // 验证DataUtil的saveProfile方法是否被调用
            dataUtilMock.verify(() -> DataUtil.saveProfile(DataUtil.currentUser.getUsername(), newEmail, DataUtil.currentUser.getPassword()));
            // 验证当前用户的邮箱是否更新
            assertEquals(newEmail, DataUtil.currentUser.getEmail());
            // 验证Dialog的alert方法是否被调用
            dialogMock.verify(() -> Dialog.alert("Profile saved successfully"));
        }
    }

    @Test
    void testUploadAvatar_ShouldCopyAvatarFileAndSetImage() throws InterruptedException {
        try (MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
            // 模拟鼠标事件
            javafx.scene.input.MouseEvent mockEvent = Mockito.mock(javafx.scene.input.MouseEvent.class);
            javafx.scene.Node mockNode = Mockito.mock(javafx.scene.Node.class);
            javafx.scene.Scene mockScene = Mockito.mock(javafx.scene.Scene.class);
            javafx.stage.Stage mockStage = Mockito.mock(javafx.stage.Stage.class);
            when(mockEvent.getSource()).thenReturn(mockNode);
            when(mockNode.getScene()).thenReturn(mockScene);
            when(mockScene.getWindow()).thenReturn(mockStage);

            // 模拟选择的文件
            File mockFile = Mockito.mock(File.class);
            when(mockFile.exists()).thenReturn(true);
            when(mockFile.toPath()).thenReturn(Paths.get("test/path/to/avatar.png"));

            // 模拟文件选择器
            javafx.stage.FileChooser mockFileChooser = Mockito.mock(javafx.stage.FileChooser.class);
            when(mockFileChooser.showOpenDialog(mockStage)).thenReturn(mockFile);

            // 在 JavaFX 应用程序线程上执行上传操作
            Platform.runLater(() -> {
                try {
                    controller.uploadAvatar(mockEvent);

                    // 验证头像文件是否被复制
                    File targetFile = new File("data/" + DataUtil.currentUser.getUsername() + "/avatar.png");
                    assertTrue(targetFile.exists());

                    // 验证头像是否设置
                    assertNotNull(avatarImageView.getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Exception occurred during test: " + e.getMessage());
                }
            });

            // 等待 JavaFX 应用程序线程执行完毕
            Thread.sleep(1000);
        }
    }
}