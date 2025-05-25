package test;

import controller.CalendarController;
import controller.HomeController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(ApplicationExtension.class)
public class CalendarControllerTest {

    private CalendarController calendarController;
    private HomeController mockHomeController;

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calendar.fxml"));
        Parent root = loader.load();
        calendarController = loader.getController();
        mockHomeController = mock(HomeController.class);
        calendarController.setHomeController(mockHomeController);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() throws Exception {
        // 在FX应用线程上设置currentYearMonth
        runAndWait(() -> {
            try {
                Field currentYearMonthField = CalendarController.class.getDeclaredField("currentYearMonth");
                currentYearMonthField.setAccessible(true);
                currentYearMonthField.set(calendarController, YearMonth.of(2025, 5));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testInitializeCurrentMonth() throws Exception {
        YearMonth currentMonth = getCurrentYearMonth();
        assertEquals(YearMonth.of(2025, 5), currentMonth);
    }

    @Test
    void testPreviousMonthButtonAction() throws Exception {
        YearMonth initialMonth = getCurrentYearMonth();

        // 在FX应用线程上触发按钮点击
        runAndWait(() -> {
            try {
                Button previousButton = getPrivateField("previousMonthButton", Button.class);
                previousButton.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals(initialMonth.minusMonths(1), getCurrentYearMonth());
        verify(mockHomeController).updateFestivalList("2025-04");
    }

    @Test
    void testNextMonthButtonAction() throws Exception {
        YearMonth initialMonth = getCurrentYearMonth();

        // 在FX应用线程上触发按钮点击
        runAndWait(() -> {
            try {
                Button nextButton = getPrivateField("nextMonthButton", Button.class);
                nextButton.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals(initialMonth.plusMonths(1), getCurrentYearMonth());
        verify(mockHomeController).updateFestivalList("2025-06");
    }

    @Test
    void testUpdateCalendarTitle() throws Exception {
        // 在FX应用线程上调用updateCalendar
        runAndWait(() -> {
            try {
                invokePrivateMethod("updateCalendar");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Label yearLabel = getPrivateField("yearLabel", Label.class);
        assertEquals("2025 - MAY", yearLabel.getText());
    }

    @Test
    void testCalendarGridDateCount() throws Exception {
        // 在FX应用线程上调用updateCalendar
        runAndWait(() -> {
            try {
                invokePrivateMethod("updateCalendar");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        GridPane calendarGrid = getPrivateField("calendarGrid", GridPane.class);
        List<javafx.scene.Node> dateLabels = calendarGrid.getChildren().stream()
                .filter(node -> node instanceof Label && !((Label) node).getText().matches("Sun|Mon|Tue|Wed|Thu|Fri|Sat"))
                .collect(Collectors.toList());

        assertEquals(31, dateLabels.size());
    }

    // ====================== 辅助方法 ======================

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
        Field field = CalendarController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(calendarController));
    }

    private void invokePrivateMethod(String methodName) throws Exception {
        java.lang.reflect.Method method = CalendarController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(calendarController);
    }

    private YearMonth getCurrentYearMonth() throws Exception {
        return getPrivateField("currentYearMonth", YearMonth.class);
    }
}