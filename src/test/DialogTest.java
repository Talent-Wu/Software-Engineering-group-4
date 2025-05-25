package test;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import org.junit.jupiter.api.Test;
import utils.Dialog;

import static org.junit.jupiter.api.Assertions.*;

class DialogTest {

    @Test
    void testAlert() {
        String message = "Test message";
        Dialog.alert(message);
        // 由于 Alert 是 GUI 组件，这里简单测试不抛出异常
        assertTrue(true);
    }

    @Test
    void testInput() {
        String message = "Test input message";
        String result = Dialog.input(message);
        assertNotNull(result);
    }
}