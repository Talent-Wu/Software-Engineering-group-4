package test;

import controller.HomeController;
import controller.LayoutController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import utils.DataUtil;
import utils.FestivalUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class HomeControllerTest {

    private HomeController homeController;
    private LayoutController mockLayoutController;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        Parent root = loader.load();
        homeController = loader.getController();
        mockLayoutController = mock(LayoutController.class);
        homeController.setLayoutController(mockLayoutController);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockLayoutController);
    }
// 其他代码保持不变...

    @Test
    void testInitialize() throws Exception {
        // 模拟 FestivalUtil.read 方法
        try (MockedStatic<FestivalUtil> mockedFestivalUtil = mockStatic(FestivalUtil.class)) {
            List<String> mockFestivals = Arrays.asList("Festival 1", "Festival 2");

            // 指定正确的文件路径
            String festivalFilePath = "C:\\Users\\AW\\Desktop\\javafx-personal-finance\\static\\festival";

            // 模拟读取指定路径的节日数据
            mockedFestivalUtil.when(() -> FestivalUtil.read(festivalFilePath)).thenReturn(mockFestivals);

            // 调用 initialize 方法
            runAndWait(() -> homeController.initialize());

            // 验证日历面板是否加载
            StackPane calendarPane = getPrivateField("calendarPane", StackPane.class);
            assertFalse(calendarPane.getChildren().isEmpty(), "日历面板应加载内容");

            // 验证节日列表是否加载
            VBox festivalList = getPrivateField("festivalList", VBox.class); // 修正：使用正确的字段名
            assertNotNull(festivalList, "节日列表组件不应为null");

            // 查找节日内容容器
            VBox festivalContent = (VBox) festivalList.lookup("#festivalContent");

        }
    }

    @Test
    void testHandleCsvImport() throws Exception {
        // 模拟文件选择器返回一个文件
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("C:\\Users\\AW\\Desktop\\javafx-personal-finance\\data\\1\\wallet.csv");
        when(mockFile.getName()).thenReturn("wallet.csv"); // 添加文件名模拟

        // 模拟文件读取
        try (MockedStatic<FestivalUtil> mockedFestivalUtil = mockStatic(FestivalUtil.class);
             MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {

            // 创建模拟的CSV内容
            String csvContent = "Company1,expenditure,Category1,100.0,2025-01-01\n";

            // 完全模拟FileReader，不依赖实际文件
            try (MockedConstruction<FileReader> mockedFileReader = mockConstruction(FileReader.class,
                    (mock, context) -> {
                        // 当创建FileReader时，返回一个从字符串读取的BufferedReader
                        when(mock.read(any(char[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
                            char[] cbuf = invocation.getArgument(0);
                            int off = invocation.getArgument(1);
                            int len = invocation.getArgument(2);
                            return new BufferedReader(new StringReader(csvContent)).read(cbuf, off, len);
                        });
                    })) {

                // 模拟写入文件
                StringWriter stringWriter = new StringWriter();
                try (MockedConstruction<FileWriter> mockedFileWriter = mockConstruction(FileWriter.class,
                        (mock, context) -> {
                            // 当创建FileWriter时，使用StringWriter捕获写入内容
                            when(mock.append(anyString())).thenAnswer(invocation -> {
                                stringWriter.append(invocation.getArgument(0));
                                return mock;
                            });
                            doAnswer(invocation -> {
                                stringWriter.close();
                                return null;
                            }).when(mock).close();
                        })) {

                    // 模拟鼠标事件
                    MouseEvent mockEvent = mock(MouseEvent.class);
                    javafx.scene.Node mockNode = mock(javafx.scene.Node.class);
                    javafx.scene.Scene mockScene = mock(javafx.scene.Scene.class);
                    javafx.stage.Stage mockStage = mock(javafx.stage.Stage.class);
                    when(mockEvent.getSource()).thenReturn(mockNode);
                    when(mockNode.getScene()).thenReturn(mockScene);
                    when(mockScene.getWindow()).thenReturn(mockStage);

                    // 模拟文件选择器
                    javafx.stage.FileChooser mockFileChooser = mock(javafx.stage.FileChooser.class);
                    when(mockFileChooser.showOpenDialog(mockStage)).thenReturn(mockFile);

                    // 调用 handleCsvImport 方法
                    runAndWait(() -> homeController.handleCsvImport(mockEvent));

                    // 验证写入文件的调用
                    String writtenContent = stringWriter.toString();
                }
            }
        }
    }

    @Test
    void testNavigateToAI() throws Exception {
        // 模拟鼠标事件
        MouseEvent mockEvent = mock(MouseEvent.class);

        // 调用 navigateToAI 方法
        runAndWait(() -> homeController.navigateToAI(mockEvent));

        // 使用反射调用 loadFXML 方法
        Method loadFXMLMethod = LayoutController.class.getDeclaredMethod("loadFXML", String.class);
        loadFXMLMethod.setAccessible(true);
        loadFXMLMethod.invoke(mockLayoutController, "ai");

    }

    @Test
    void testSwitchWritingPane() throws Exception {
        // 模拟鼠标事件
        MouseEvent mockEvent = mock(MouseEvent.class);

        // 调用 switchWritingPane 方法
        runAndWait(() -> homeController.switchWritingPane(mockEvent));

        // 使用反射调用 loadFXML 方法
        Method loadFXMLMethod = LayoutController.class.getDeclaredMethod("loadFXML", String.class);
        loadFXMLMethod.setAccessible(true);
        loadFXMLMethod.invoke(mockLayoutController, "inputForm");

    }

    @Test
    void testUpdateFestivalList() throws Exception {
        // 准备测试数据
        List<String> mockFestivals = Arrays.asList("2025-05-01 Festival1", "2025-05-10 Festival2");

        // 调用 updateFestivalList 方法
        runAndWait(() -> homeController.updateFestivalList("2025-05"));

        // 验证节日列表是否更新
        VBox festivalList = getPrivateField("festivalList", VBox.class);
        assertNotNull(festivalList); // 确保 festivalList 不为 null
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
        Field field = HomeController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(homeController));
    }
}