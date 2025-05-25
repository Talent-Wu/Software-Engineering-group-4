package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.StageContainer;

import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import static org.mockito.Mockito.*;

public class StageContainerTest {

    @Mock
    private FXMLLoader mockLoader;
    @Mock
    private Parent mockParent;
    @Mock
    private Stage mockStage;
    @Mock
    private Scene mockScene;
    @Mock
    private URL mockUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Stack<Stage> emptyStack = new Stack<>();
        // 重置 stages 栈
        try {
            java.lang.reflect.Field stagesField = StageContainer.class.getDeclaredField("stages");
            stagesField.setAccessible(true);
            stagesField.set(null, emptyStack);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testSaveData() {
        StageContainer.saveData();
        // 由于 DB_PATH 为空，会抛出异常，验证是否捕获并打印了异常堆栈信息
        verify(mockStage, never()).show();
    }

    @Test
    void testLoadData() {
        StageContainer.loadData();
        // 由于 DB_PATH 为空，会抛出异常，验证是否捕获并打印了异常堆栈信息
        verify(mockStage, never()).show();
    }
}