package test;

import org.junit.jupiter.api.Test;
import utils.FestivalUtil;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FestivalUtilTest {

    @Test
    public void testReadFestivalFile() {
        // 测试读取节日信息文件的功能
        List<String> festivals = FestivalUtil.read("2021-02");
        assertFalse(festivals.isEmpty());
        assertTrue(festivals.contains("2021-02-12 The Spring Festival"));
        assertTrue(festivals.contains("2021-02-26 The Lantern Festival"));
    }
}