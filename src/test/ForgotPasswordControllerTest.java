package test;

import controller.ForgotPasswordController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
import utils.StageContainer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class ForgotPasswordControllerTest {

    private TextField nameField;
    private TextField emailField;
    private PasswordField newPasswordField;
    private Button resetButton;
    private Button backButton;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forgotPassword.fxml"));
        Parent root = loader.load();
        ForgotPasswordController controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();

        // 获取FXML中的控件
        nameField = (TextField) root.lookup("#nameField");
        emailField = (TextField) root.lookup("#emailField");
        newPasswordField = (PasswordField) root.lookup("#newPasswordField");
        resetButton = (Button) root.lookup("#resetButton");
        backButton = (Button) root.lookup("#backButton");
    }

    @BeforeEach
    void setUp() {
        // 清除所有mock的静态方法调用
        Mockito.reset();
    }

    @Test
    void testOnResetPasswordAction_UserExists() throws Exception {
        String username = "testuser";
        String email = "testuser@gmail.com";
        String newPassword = "newpassword";

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // 设置输入
            nameField.setText(username);
            emailField.setText(email);
            newPasswordField.setText(newPassword);

            try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class);
                 MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class);
                 MockedStatic<StageContainer> stageContainerMock = mockStatic(StageContainer.class)) {

                User user = new User(email, "oldpassword", username);
                dataUtilMock.when(() -> DataUtil.readProfile(username)).thenReturn(user);

                // 执行测试
                resetButton.fire();

                // 验证结果
                dataUtilMock.verify(() -> DataUtil.readProfile(username), times(1));
                dataUtilMock.verify(() -> DataUtil.saveProfile(username, email, newPassword), times(1));
                dialogMock.verify(() -> Dialog.alert("Password reset to " + newPassword), times(1));
                stageContainerMock.verify(() -> StageContainer.switchStage("login"), times(1));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testOnBackAction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try (MockedStatic<StageContainer> stageContainerMock = mockStatic(StageContainer.class)) {
                // 执行测试
                backButton.fire();

                // 验证结果
                stageContainerMock.verify(() -> StageContainer.switchStage("login"), times(1));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}