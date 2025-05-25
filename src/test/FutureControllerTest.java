package test;

import controller.FutureController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.User;
import model.Wallet;
import model.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import utils.DataUtil;
import utils.Dialog;
import utils.WishCalculater;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class FutureControllerTest {

    private FutureController futureController;
    private ProgressBar mockProgressBar;
    private TableView<Wish> mockTable;
    private TableColumn<Wish, String> mockNameColumn;
    private TableColumn<Wish, Double> mockAmountColumn;
    private TableColumn<Wish, String> mockCompleteColumn;
    private Label mockGoalLabel;
    private Label mockCompleteGoalPercent;
    private TextField mockWishName;
    private TextField mockWishAmount;
    private ObservableList<Wish> mockData;

    @BeforeEach
    public void setUp() throws Exception {
        futureController = new FutureController();
        mockProgressBar = mock(ProgressBar.class);
        mockTable = mock(TableView.class);
        mockNameColumn = mock(TableColumn.class);
        mockAmountColumn = mock(TableColumn.class);
        mockCompleteColumn = mock(TableColumn.class);
        mockGoalLabel = mock(Label.class);
        mockCompleteGoalPercent = mock(Label.class);
        mockWishName = mock(TextField.class);
        mockWishAmount = mock(TextField.class);

        // 初始化模拟数据
        mockData = FXCollections.observableArrayList();
        when(mockTable.getItems()).thenReturn(mockData);

        // 设置控制器的字段
        futureController.progressBar = mockProgressBar;
        futureController.table = mockTable;
        futureController.nameColumn = mockNameColumn;
        futureController.amountColumn = mockAmountColumn;
        futureController.completeColumn = mockCompleteColumn;
        futureController.goalLabel = mockGoalLabel;
        futureController.completeGoalPercent = mockCompleteGoalPercent;
        futureController.wishName = mockWishName;
        futureController.wishAmount = mockWishAmount;

        // 模拟当前用户
        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("testUser");

        // 使用反射设置当前用户
        Field currentUserField = DataUtil.class.getDeclaredField("currentUser");
        currentUserField.setAccessible(true);
        currentUserField.set(null, mockUser);
    }

    @Test
    public void testInitialize() throws Exception {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class);
             MockedStatic<WishCalculater> wishCalculaterMock = mockStatic(WishCalculater.class)) {

            // 模拟读取目标金额
            dataUtilMock.when(() -> DataUtil.readData("testUser/goal.txt", false)).thenReturn("1000");

            // 模拟计算愿望完成情况
            List<Wish> mockWishes = new ArrayList<>();
            wishCalculaterMock.when(() -> WishCalculater.calculateComplete(anyList(), anyList(), anyList()))
                    .thenReturn(mockWishes);

            // 调用initialize方法
            futureController.initialize();

            // 验证表格列是否设置了单元格值工厂
            verify(mockNameColumn).setCellValueFactory(any());
            verify(mockAmountColumn).setCellValueFactory(any());
            verify(mockCompleteColumn).setCellValueFactory(any());

            // 验证数据是否加载
            verify(mockTable).setItems(mockData);
            assertTrue(mockData.isEmpty()); // 因为我们模拟了空的愿望列表

            // 验证是否调用了计算目标的方法
            verify(mockGoalLabel).setText("Goal: $" + String.format("%.2f", 1000.0));
        }
    }
    @Test
    public void testCalcGoal_WithValidGoal() throws Exception {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            String goalStr = "1000";
            double goal = Double.parseDouble(goalStr);

            // 模拟收入和支出数据
            List<Wallet> mockIncomeList = new ArrayList<>();
            String validDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            mockIncomeList.add(new Wallet("Salary", 5000, "Company", validDate));

            List<Wallet> mockExpenseList = new ArrayList<>();
            mockExpenseList.add(new Wallet("Rent", -2000, "Landlord", validDate));

            double netIncome = 3000;
            double percent = netIncome / goal;

            // 设置模拟返回值
            dataUtilMock.when(() -> DataUtil.readData("testUser/goal.txt", false)).thenReturn(goalStr);
            dataUtilMock.when(DataUtil::readIncome).thenReturn(mockIncomeList);
            dataUtilMock.when(DataUtil::readExpense).thenReturn(mockExpenseList);

            // 调用私有方法
            Method calcGoalMethod = FutureController.class.getDeclaredMethod("calcGoal");
            calcGoalMethod.setAccessible(true);
            calcGoalMethod.invoke(futureController);

            // 验证UI更新
            verify(mockGoalLabel).setText("Goal: $" + String.format("%.2f", goal));
        }
    }

    @Test
    public void testCalcGoal_WithZeroGoal() throws Exception {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            // 模拟目标金额为0
            dataUtilMock.when(() -> DataUtil.readData("testUser/goal.txt", false)).thenReturn("0");

            // 模拟收入和支出数据
            List<Wallet> mockIncomeList = new ArrayList<>();
            String validDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            mockIncomeList.add(new Wallet("Salary", 5000, "Company", validDate));

            List<Wallet> mockExpenseList = new ArrayList<>();
            mockExpenseList.add(new Wallet("Rent", -2000, "Landlord", validDate));

            // 设置模拟返回值
            dataUtilMock.when(DataUtil::readIncome).thenReturn(mockIncomeList);
            dataUtilMock.when(DataUtil::readExpense).thenReturn(mockExpenseList);

            // 调用私有方法
            Method calcGoalMethod = FutureController.class.getDeclaredMethod("calcGoal");
            calcGoalMethod.setAccessible(true);
            calcGoalMethod.invoke(futureController);

            // 验证UI更新（百分比应为0%）
            verify(mockGoalLabel).setText("Goal: $" + String.format("%.2f", 0.0));
            verify(mockCompleteGoalPercent).setText(String.format("%.2f%%", 0.0));
            verify(mockProgressBar).setProgress(0);
        }
    }

    @Test
    public void testSetTarget() throws Exception {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class);
             MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
            String goal = "2000";
            dialogMock.when(() -> Dialog.input(anyString())).thenReturn(goal);

            // 模拟 DataUtil.readData 方法，确保返回一个有效的字符串值
            dataUtilMock.when(() -> DataUtil.readData("testUser/goal.txt", false)).thenReturn(goal);

            // 模拟鼠标事件
            MouseEvent mockEvent = mock(MouseEvent.class);

            // 调用 setTarget 方法
            futureController.setTarget(mockEvent);

            // 验证数据保存
            dataUtilMock.verify(() -> DataUtil.saveData("goal.txt", goal), times(1));

            // 验证目标计算方法被调用
            verify(mockGoalLabel).setText("Goal: $" + String.format("%.2f", Double.parseDouble(goal)));
        }
    }

    @Test
    public void testReset() throws Exception {
        try (MockedStatic<DataUtil> dataUtilMock = mockStatic(DataUtil.class)) {
            // 模拟 DataUtil.readData 方法，确保返回一个有效的字符串值
            dataUtilMock.when(() -> DataUtil.readData("testUser/goal.txt", false)).thenReturn("0");

            MouseEvent mockEvent = mock(MouseEvent.class);
            futureController.reset(mockEvent);

            // 验证数据保存
            dataUtilMock.verify(() -> DataUtil.saveData("goal.txt", "0"), times(1));

            // 验证目标计算方法被调用
            verify(mockGoalLabel).setText("Goal: $" + String.format("%.2f", 0.0));
        }
    }


    @Test
    public void testAddWishlist_InvalidAmount() {
        try (MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
            String name = "Wish Name";
            String amountStr = "invalid";

            when(mockWishName.getText()).thenReturn(name);
            when(mockWishAmount.getText()).thenReturn(amountStr);

            MouseEvent mockEvent = mock(MouseEvent.class);
            futureController.addWishlist(mockEvent);

            dialogMock.verify(() -> Dialog.alert("Invalid amount! Please enter a valid number."));
            assertTrue(mockData.isEmpty()); // 验证没有添加愿望
        }
    }

    @Test
    public void testAddWishlist_EmptyInput() {
        try (MockedStatic<Dialog> dialogMock = mockStatic(Dialog.class)) {
            String name = "";
            String amountStr = "";

            when(mockWishName.getText()).thenReturn(name);
            when(mockWishAmount.getText()).thenReturn(amountStr);

            MouseEvent mockEvent = mock(MouseEvent.class);
            futureController.addWishlist(mockEvent);

            dialogMock.verify(() -> Dialog.alert("Wish name and amount cannot be empty."));
            assertTrue(mockData.isEmpty()); // 验证没有添加愿望
        }
    }

}