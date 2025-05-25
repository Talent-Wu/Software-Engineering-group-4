package test;

import controller.InputFormController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DataUtil;
import utils.Dialog;

public class InputFormControllerTest {
    private InputFormController controller;

    @BeforeEach
    void setUp() {
        controller = new InputFormController();
    }

    @Test
    void testSubmit() {
        // 假设这里需要调用 Dialog 和 DataUtil 的方法
        String message = "Some test message";
        Dialog.alert(message); // 假设这里需要调用 Dialog 的 alert 方法

        DataUtil.saveWallet("income", "pet", "100", "CompanyA");  // 调用 DataUtil 的 saveWallet 方法

        String inputMessage = "Please enter some input";
        String inputResult = Dialog.input(inputMessage); // 假设这里需要调用 Dialog 的 input 方法

        // 其他测试逻辑
    }
}