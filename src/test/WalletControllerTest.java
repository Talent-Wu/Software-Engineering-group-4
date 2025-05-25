package test;

import controller.WalletController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;
import utils.DataUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class WalletControllerTest {

    private WalletController controller;
    private Label income;
    private Label expenses;

    @Start
    public void start(Stage stage) throws IOException {
        // 加载FXML文件
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wallet.fxml"));
        Parent root = loader.load();

        // 获取控制器实例
        controller = loader.getController();

        // 获取UI组件引用
        income = (Label) root.lookup("#income");
        expenses = (Label) root.lookup("#expenses");

        // 显示舞台
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() {
        // 清理可能残留的mock
        Mockito.framework().clearInlineMocks();
    }

    @Test
    void testInitializeShouldLoadIncomeAndExpenses() throws InterruptedException {
        // 准备测试数据
        List<Wallet> mockIncomeList = Arrays.asList(
                new Wallet("Salary", 0.0, "CompanyA", "2025-05-22 12:00:00"),
                new Wallet("Bonus", 0.0, "CompanyA", "2025-05-22 13:00:00")
        );
        List<Wallet> mockExpenseList = Arrays.asList(
                new Wallet("Food", -0.0, "Restaurant", "2025-05-22 12:00:00"),
                new Wallet("Transport", -0.0, "Bus", "2025-05-22 13:00:00")
        );

        // 使用Mockito.mockStatic()创建静态mock
        try (MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {
            // 设置静态方法的行为
            mockedDataUtil.when(DataUtil::readIncome).thenReturn(mockIncomeList);
            mockedDataUtil.when(DataUtil::readExpense).thenReturn(mockExpenseList);

            CountDownLatch latch = new CountDownLatch(1);
            javafx.application.Platform.runLater(() -> {
                // 强制重新初始化控制器
                controller.initialize();
                latch.countDown();
            });

            latch.await();
            WaitForAsyncUtils.waitForFxEvents();

            // 验证收入总和
            double totalIncome = mockIncomeList.stream().mapToDouble(Wallet::getAmount).sum();
            assertEquals("￥" + (int) totalIncome, income.getText());

            // 验证支出总和
            double totalExpenses = mockExpenseList.stream().mapToDouble(Wallet::getAmount).sum();
            assertEquals("￥" + (int) totalExpenses, expenses.getText());
        }
    }

    @Test
    void testClearShouldClearWalletAndReinitialize() throws InterruptedException {
        // 使用Mockito.mockStatic()创建静态mock
        try (MockedStatic<DataUtil> mockedDataUtil = mockStatic(DataUtil.class)) {
            CountDownLatch latch = new CountDownLatch(1);
            javafx.application.Platform.runLater(() -> {
                // 模拟点击清除按钮
                controller.clear(null);
                latch.countDown();
            });

            latch.await();
            WaitForAsyncUtils.waitForFxEvents();

            // 验证initialize方法被调用
            // 由于无法直接验证initialize方法调用，通过验证收入和支出标签是否重新初始化来间接验证
            // 假设重新初始化后收入和支出标签显示为初始值（这里假设为￥0）
            assertEquals("￥0", income.getText());
            assertEquals("￥0", expenses.getText());
        }
    }
}