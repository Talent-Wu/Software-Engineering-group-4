package test;

import controller.IncomeController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import utils.DataUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class IncomeControllerTest {

    private IncomeController controller;
    private TableView<Wallet> tableView;
    private BarChart<String, Number> barChart;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/income.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        tableView = (TableView<Wallet>) root.lookup("#table");
        barChart = (BarChart<String, Number>) root.lookup("#chart");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() {
        // 清理 Mockito 的 inline mock
        Mockito.framework().clearInlineMocks();
    }

    @Test
    void testInitialize() throws InterruptedException {
        // 准备测试数据
        List<Wallet> mockIncomes = Arrays.asList(
                new Wallet("Salary", 5000, "Company A", "2025-06-01 09:00:00"),
                new Wallet("Bonus", 1000, "Company A", "2025-06-15 15:00:00")
        );

        // 模拟 DataUtil 的 readIncome 方法
        try (MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {
            mockedDataUtil.when(DataUtil::readIncome).thenReturn(mockIncomes);

            // 在 FX 应用线程上执行初始化操作
            runOnFxThreadAndWait(() -> controller.initialize());

            // 验证表格数据
            assertEquals(mockIncomes.size(), tableView.getItems().size());
            for (int i = 0; i < mockIncomes.size(); i++) {
                assertEquals(mockIncomes.get(i).getCategory(), tableView.getItems().get(i).getCategory());
                assertEquals(mockIncomes.get(i).getAmount(), tableView.getItems().get(i).getAmount(), 0.001);
            }

            // 验证图表数据
            assertEquals(1, barChart.getData().size());
            XYChart.Series<String, Number> series = barChart.getData().get(0);
            assertEquals(mockIncomes.size(), series.getData().size());
            for (int i = 0; i < mockIncomes.size(); i++) {
                assertEquals(mockIncomes.get(i).getCategory(), series.getData().get(i).getXValue());
                assertEquals((float) mockIncomes.get(i).getAmount(), (Float) series.getData().get(i).getYValue(), 0.001);
            }

            // 验证 DataUtil.readIncome 方法被调用
            mockedDataUtil.verify(DataUtil::readIncome);
        }
    }

    // 辅助方法：在 JavaFX 线程上运行任务并等待完成
    private void runOnFxThreadAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}