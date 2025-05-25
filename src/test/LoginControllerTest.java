package test;

import controller.LoginController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;
import utils.DataUtil;
import utils.Dialog;
import utils.StageContainer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {

    private LoginController controller;
    private TextField username;
    private CheckBox showPasswordCheckBox;
    private PasswordField passwordField;
    private TextField passwordTextField;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();

        // 获取FXML中的控件
        username = (TextField) root.lookup("#username");
        showPasswordCheckBox = (CheckBox) root.lookup("#showPasswordCheckBox");
        passwordField = (PasswordField) root.lookup("#passwordField");
        passwordTextField = (TextField) root.lookup("#passwordTextField");
    }

    @BeforeEach
    void setUp() {
        // 清除所有mock的静态方法调用
        Mockito.reset();
    }

    @Test
    void testOnLogin_UserExistsAndPasswordCorrect() throws Exception {
        String name = "testuser";
        String password = "123";

        // 设置输入
        username.setText(name);
        passwordField.setText(password);

        // mock DataUtil类的静态方法
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            User user = new User("testuser@gmail.com", password, name);
            dataUtilMock.when(() -> DataUtil.readProfile(name)).thenReturn(user);

            // mock StageContainer类的静态方法
            try (MockedStatic<StageContainer> stageContainerMock = mockStatic(StageContainer.class)) {
                // 执行测试
                MouseEvent mockEvent = mock(MouseEvent.class);
                controller.onLogin(mockEvent);

                // 验证结果
                dataUtilMock.verify(() -> DataUtil.readProfile(name));
                stageContainerMock.verify(() -> StageContainer.switchStage("layout"));
            }
        }
    }

    @Test
    void testOnLogin_UserExistsAndPasswordWrong() throws Exception {
        String name = "testuser";
        String wrongPassword = "wrongpassword";

        // 设置输入
        username.setText(name);
        passwordField.setText(wrongPassword);

        // mock DataUtil类的静态方法
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            User user = new User("testuser@gmail.com", "123", name);
            dataUtilMock.when(() -> DataUtil.readProfile(name)).thenReturn(user);

            // mock Dialog类的静态方法
            try (MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
                // 执行测试
                MouseEvent mockEvent = mock(MouseEvent.class);
                controller.onLogin(mockEvent);

                // 验证结果
                dataUtilMock.verify(() -> DataUtil.readProfile(name));
                dialogMock.verify(() -> Dialog.alert("Password is wrong!"));
                verifyNoInteractions(StageContainer.class);
            }
        }
    }

    @Test
    void testOnLogin_UserNotExists() throws Exception {
        String name = "nonExistentUser";
        String password = "123";

        // 在 JavaFX 应用程序线程中设置输入
        Platform.runLater(() -> {
            username.setText(name);
            passwordField.setText(password);
        });
        // 等待 JavaFX 应用程序线程执行完毕
        WaitForAsyncUtils.waitForFxEvents();

        // mock DataUtil 类的静态方法
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            dataUtilMock.when(() -> DataUtil.readProfile(name)).thenReturn(null);

            // mock Dialog 类的静态方法
            try (MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
                // 在 JavaFX 应用程序线程中执行测试
                Platform.runLater(() -> {
                    MouseEvent mockEvent = mock(MouseEvent.class);
                    controller.onLogin(mockEvent);
                });
                // 等待 JavaFX 应用程序线程执行完毕
                WaitForAsyncUtils.waitForFxEvents();

                // 验证结果
                dataUtilMock.verify(() -> DataUtil.readProfile(name));
                dialogMock.verify(() -> Dialog.alert("Username is wrong!"));
                verifyNoInteractions(StageContainer.class);
            }
        }
    }

    @Test
    void testOnLogin_ShowPasswordSelected() throws Exception {
        String name = "testuser";
        String password = "123";

        // 设置输入
        username.setText(name);
        showPasswordCheckBox.setSelected(true);
        passwordTextField.setText(password);

        // mock DataUtil类的静态方法
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            User user = new User("testuser@gmail.com", password, name);
            dataUtilMock.when(() -> DataUtil.readProfile(name)).thenReturn(user);

            // mock StageContainer类的静态方法
            try (MockedStatic<StageContainer> stageContainerMock = mockStatic(StageContainer.class)) {
                // 执行测试
                MouseEvent mockEvent = mock(MouseEvent.class);
                controller.onLogin(mockEvent);

                // 验证结果
                dataUtilMock.verify(() -> DataUtil.readProfile(name));
                stageContainerMock.verify(() -> StageContainer.switchStage("layout"));
            }
        }
    }

    @Test
    void testOnSignUp() {
        // mock StageContainer类的静态方法
        try (MockedStatic<StageContainer> stageContainerMock = mockStatic(StageContainer.class)) {
            // 执行测试
            MouseEvent mockEvent = mock(MouseEvent.class);
            controller.onSignUp(mockEvent);

            // 验证结果
            stageContainerMock.verify(() -> StageContainer.switchStage("signup"));
        }
    }

    @Test
    void testTogglePasswordVisibility_ShowPassword() {
        showPasswordCheckBox.setSelected(true);
        passwordField.setText("testpassword");

        // 执行测试
        javafx.event.ActionEvent mockEvent = mock(javafx.event.ActionEvent.class);
        controller.togglePasswordVisibility(mockEvent);

        // 验证结果
        assertEquals("testpassword", passwordTextField.getText());
        assertTrue(passwordTextField.isVisible());
        assertFalse(passwordField.isVisible());
    }

    @Test
    void testTogglePasswordVisibility_HidePassword() {
        showPasswordCheckBox.setSelected(false);
        passwordTextField.setText("testpassword");

        // 执行测试
        javafx.event.ActionEvent mockEvent = mock(javafx.event.ActionEvent.class);
        controller.togglePasswordVisibility(mockEvent);

        // 验证结果
        assertEquals("testpassword", passwordField.getText());
        assertTrue(passwordField.isVisible());
        assertFalse(passwordTextField.isVisible());
    }

    @Test
    void testOnForgotPasswordAction() {
        // mock StageContainer类的静态方法
        try (MockedStatic<StageContainer> stageContainerMock = mockStatic(StageContainer.class)) {
            // 执行测试
            javafx.event.ActionEvent mockEvent = mock(javafx.event.ActionEvent.class);
            controller.onForgotPasswordAction(mockEvent);

            // 验证结果
            stageContainerMock.verify(() -> StageContainer.switchStage("forgotPassword"));
        }
    }
}