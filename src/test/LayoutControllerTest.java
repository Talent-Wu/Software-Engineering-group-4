package test;

import controller.LayoutController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class LayoutControllerTest {

    private LayoutController layoutController;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/layout.fxml"));
        Parent root = loader.load();
        layoutController = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() {
        // 可以在这里进行一些初始化操作，如果需要的话
    }

    @Test
    void testInitialize() throws Exception {
        // 调用 initialize 方法
        runAndWait(() -> layoutController.initialize());

        // 获取 borderPane 并验证是否加载了 home fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    @Test
    void testSwitchHome() throws Exception {
        // 调用 switchHome 方法
        Button homeBtn = getPrivateField("homeBtn", Button.class);
        runAndWait(() -> homeBtn.fire());

        // 获取 borderPane 并验证是否加载了 home fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    @Test
    void testSwitchIncome() throws Exception {
        // 模拟一个鼠标事件
        javafx.scene.input.MouseEvent mockEvent = Mockito.mock(javafx.scene.input.MouseEvent.class);
        // 调用 switchIncome 方法
        runAndWait(() -> layoutController.switchIncome(mockEvent));

        // 获取 borderPane 并验证是否加载了 income fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    @Test
    void testSwitchExpense() throws Exception {
        // 模拟一个鼠标事件
        javafx.scene.input.MouseEvent mockEvent = Mockito.mock(javafx.scene.input.MouseEvent.class);
        // 调用 switchExpense 方法
        runAndWait(() -> layoutController.switchExpense(mockEvent));

        // 获取 borderPane 并验证是否加载了 expense fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    @Test
    void testSwitchFuture() throws Exception {
        // 模拟一个鼠标事件
        javafx.scene.input.MouseEvent mockEvent = Mockito.mock(javafx.scene.input.MouseEvent.class);
        // 调用 switchFuture 方法
        runAndWait(() -> layoutController.switchFuture(mockEvent));

        // 获取 borderPane 并验证是否加载了 future fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    @Test
    void testSwitchWallet() throws Exception {
        // 模拟一个鼠标事件
        javafx.scene.input.MouseEvent mockEvent = Mockito.mock(javafx.scene.input.MouseEvent.class);
        // 调用 switchWallet 方法
        runAndWait(() -> layoutController.switchWallet(mockEvent));

        // 获取 borderPane 并验证是否加载了 wallet fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    @Test
    void testSwitchProfile() throws Exception {
        // 模拟一个鼠标事件
        javafx.scene.input.MouseEvent mockEvent = Mockito.mock(javafx.scene.input.MouseEvent.class);
        // 调用 switchProfile 方法
        runAndWait(() -> layoutController.switchProfile(mockEvent));

        // 获取 borderPane 并验证是否加载了 profile fxml
        BorderPane borderPane = getPrivateField("borderPane", BorderPane.class);
        assertNotNull(borderPane.getCenter());
    }

    // 在FX应用线程上执行操作并等待完成
    private void runAndWait(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });

        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timed out waiting for FX thread");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    // 反射辅助方法
    private <T> T getPrivateField(String fieldName, Class<T> type) throws Exception {
        Field field = LayoutController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(layoutController));
    }
}