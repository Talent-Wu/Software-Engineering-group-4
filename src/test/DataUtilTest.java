package test;

import model.Wallet;
import model.User;
import model.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utils.DataUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {

    @BeforeEach
    void setUp() {
        // 初始化当前用户
        DataUtil.currentUser = new User("testuser@gmail.com", "123", "Johns");
        // 清理可能存在的测试文件
        File walletFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wallet.csv");
        File wishlistFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wishlist.csv");
        File profileFile = new File("data/" + DataUtil.currentUser.getUsername() + "/profile.csv");
        File goalFile = new File("data/" + DataUtil.currentUser.getUsername() + "/goal.txt");
        walletFile.delete();
        wishlistFile.delete();
        profileFile.delete();
        goalFile.delete();

        // 创建用户和相关文件
        DataUtil.createUserAndFiles("Johns", "testuser@gmail.com", "123");
    }

    @Test
    void testGetCurrentTime() {
        String time = DataUtil.getCurrentTime();
        assertNotNull(time);
    }

    @Test
    void testSaveWallet() {
        DataUtil.saveWallet("income", "Salary", "5000", "CompanyA");
        File walletFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wallet.csv");
        assertTrue(walletFile.exists());
    }

    @Test
    void testClearWallet() {
        DataUtil.clearWallet();
        File walletFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wallet.csv");
        assertTrue(walletFile.exists());
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(walletFile))) {
            String line = reader.readLine();
            assertEquals("Amount,Category,Company,Time", line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSaveWish() {
        DataUtil.saveWish("New Wish", "1000");
        File wishlistFile = new File("data/" + DataUtil.currentUser.getUsername() + "/wishlist.csv");
        assertTrue(wishlistFile.exists());
    }

    @Test
    void testSaveProfile() {
        DataUtil.saveProfile("Johns", "testuser@gmail.com", "123");
        File profileFile = new File("data/Johns/profile.csv");
        assertTrue(profileFile.exists());
    }

    @Test
    void testReadProfile() {
        DataUtil.saveProfile("Johns", "testuser@gmail.com", "123");
        User user = DataUtil.readProfile("Johns");
        assertNotNull(user);
        assertEquals("testuser@gmail.com", user.getEmail());
        assertEquals("123", user.getPassword());
        assertEquals("Johns", user.getUsername());
    }

    @Test
    void testCreateUserAndFiles() {
        String username = "newUser";
        String email = "newuser@gmail.com";
        String password = "123";

        // 先删除可能存在的测试目录，确保测试环境干净
        File userDir = new File("data/" + username);
        if (userDir.exists()) {
            deleteDirectory(userDir);
        }

        boolean result = DataUtil.createUserAndFiles(username, email, password);
        System.out.println("createUserAndFiles result: " + result); // 打印创建结果

        assertTrue(result);

        userDir = new File("data/" + username);
        System.out.println("User directory exists: " + userDir.exists()); // 打印用户目录是否存在

        assertTrue(userDir.exists());

        File walletFile = new File("data/" + username + "/wallet.csv");
        File wishlistFile = new File("data/" + username + "/wishlist.csv");
        File profileFile = new File("data/" + username + "/profile.csv");
        File goalFile = new File("data/" + username + "/goal.txt");

        System.out.println("Wallet file exists: " + walletFile.exists()); // 打印钱包文件是否存在
        System.out.println("Wishlist file exists: " + wishlistFile.exists()); // 打印愿望清单文件是否存在
        System.out.println("Profile file exists: " + profileFile.exists()); // 打印用户资料文件是否存在
        System.out.println("Goal file exists: " + goalFile.exists()); // 打印目标文件是否存在

        assertTrue(walletFile.exists());
        assertTrue(wishlistFile.exists());
        assertTrue(profileFile.exists());
        assertTrue(goalFile.exists());
    }

    // 递归删除目录及其内容
    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }
    @Test
    void testSaveData() {
        DataUtil.saveData("goal.txt", "2000");
        File goalFile = new File("data/" + DataUtil.currentUser.getUsername() + "/goal.txt");
        assertTrue(goalFile.exists());
    }

    @Test
    void testReadData() {
        DataUtil.saveData("goal.txt", "2000");
        String data = DataUtil.readData(DataUtil.currentUser.getUsername() + "/goal.txt", false);
        assertEquals("2000", data);
    }

}