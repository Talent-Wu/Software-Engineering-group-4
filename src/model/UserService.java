package model;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static List<User> users = new ArrayList<>();

    // 添加用户
    public static void addUser(User user) {
        users.add(user);
    }

    // 根据用户名查找用户
    public static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // 验证用户名和密码
    public static boolean validateUser(String username, String password) {
        User user = findUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    // 静态初始化块，添加预定义的用户
    static {
        // 添加一些预定义的用户
        addUser(new User("Admin", "123", "admin@example.com"));
    }
}