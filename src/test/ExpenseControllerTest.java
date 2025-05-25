package test;

import controller.ExpenseController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
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
public class ExpenseControllerTest {

    private ExpenseController controller;
    private TableView<Wallet> table;
    private PieChart chart;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/expense.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        table = (TableView<Wallet>) root.lookup("#table");
        chart = (PieChart) root.lookup("#chart");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() {
        Mockito.framework().clearInlineMocks();
    }

    @Test
    void testInitializeShouldLoadExpenses() throws InterruptedException {
        // 准备测试数据
        List<Wallet> mockExpenses = Arrays.asList(
                new Wallet("Food", -200.0, "Restaurant", "2025-05-22 12:00:00"),
                new Wallet("Transport", -100.0, "Bus", "2025-05-22 13:00:00")
        );

        // 模拟 DataUtil 的 readExpense 方法
        try (MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {
            mockedDataUtil.when(DataUtil::readExpense).thenReturn(mockExpenses);

            // 在 FX 应用线程上执行初始化操作
            runOnFxThreadAndWait(() -> controller.initialize());

            // 验证表格数据
            assertEquals(mockExpenses.size(), table.getItems().size());
            for (int i = 0; i < mockExpenses.size(); i++) {
                assertEquals(mockExpenses.get(i).getCategory(), table.getItems().get(i).getCategory());
                assertEquals(mockExpenses.get(i).getAmount(), table.getItems().get(i).getAmount(), 0.001);
            }

            // 验证 DataUtil.readExpense 方法被调用
            mockedDataUtil.verify(DataUtil::readExpense);
        }
    }

    @Test
    void testChartShouldShowCorrectCategoryDistribution() throws InterruptedException {
        // 准备测试数据
        List<Wallet> mockExpenses = Arrays.asList(
                new Wallet("Food", -200.0, "Restaurant", "2025-05-22 12:00:00"),
                new Wallet("Transport", -100.0, "Bus", "2025-05-22 13:00:00")
        );

        // 模拟 DataUtil 的 readExpense 方法
        try (MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {
            mockedDataUtil.when(DataUtil::readExpense).thenReturn(mockExpenses);

            // 在 FX 应用线程上执行初始化操作
            runOnFxThreadAndWait(() -> {
                controller.initialize();
                // 添加调试信息，确保 initialize 方法被调用
                System.out.println("Controller initialized");
            });

            // 确保图表数据已加载
            runOnFxThreadAndWait(() -> {
                // 添加调试信息，确保图表数据已加载
                System.out.println("Chart data size: " + chart.getData().size());
            });

            // 验证图表数据
            ObservableList<PieChart.Data> chartData = chart.getData();
            for (PieChart.Data data : chartData) {
                String category = data.getName();
                double amount = data.getPieValue();
                for (Wallet expense : mockExpenses) {
                    if (expense.getCategory().equals(category)) {
                        assertEquals(Math.abs(expense.getAmount()), amount, 0.001, "Amount for category " + category + " should match");
                    }
                }
            }
        }
    }


    @Test
    void testDeleteExpenseShouldRemoveFromTableAndUpdateChart() throws InterruptedException {
        // 准备测试数据
        List<Wallet> mockExpenses = Arrays.asList(
                new Wallet("Food", -200.0, "Restaurant", "2025-05-22 12:00:00"),
                new Wallet("Transport", -100.0, "Bus", "2025-05-22 13:00:00")
        );

        // 模拟 DataUtil 的 readExpense 方法
        try (MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {
            mockedDataUtil.when(DataUtil::readExpense).thenReturn(mockExpenses);

            // 在 FX 应用线程上执行初始化操作
            runOnFxThreadAndWait(() -> controller.initialize());

            // 验证表格列数
            assertEquals(3, table.getColumns().size());

            // 选择要删除的第一项
            table.getSelectionModel().select(0);

            // 模拟删除操作
            // 这里需要根据实际的删除逻辑调用相应的方法
            // 假设删除逻辑在 controller 中有一个 deleteExpense 方法
            // runOnFxThreadAndWait(() -> controller.deleteExpense());

            // 验证表格数据
            assertEquals(mockExpenses.size() - 1, table.getItems().size());
            assertEquals("Transport", table.getItems().get(0).getCategory());

            // 验证图表数据
            assertEquals(1, chart.getData().size());
            PieChart.Data remainingData = chart.getData().get(0);
            assertEquals("Transport", remainingData.getName());
            assertEquals(100.0, remainingData.getPieValue(), 0.001);
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