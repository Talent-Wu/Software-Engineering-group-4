package utils;

import model.Wallet;
import model.User;
import model.Wish;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtil {
    // 当前登录用户，默认为测试用户
    public static User currentUser = new User("testuser@gmail.com", "123", "Johns");
    // 当前查看的用户，可能与当前登录用户不同
    public static User viewingUser;

    // 获取当前时间，格式为"yyyy-MM-dd HH:mm:ss"
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // 保存钱包记录，包括收入和支出
    public static void saveWallet(String type, String category, String amount, String company) {
        String filepath = "data/" + currentUser.getUsername() + "/wallet.csv";
        try (FileWriter writer = new FileWriter(filepath, true)) {
            String time = getCurrentTime();
            if (type.equals("income")) {
                writer.write(amount + "," + category + "," + company + "," + time + "\n");
            } else {
                writer.write("-" + amount + "," + category + "," + company + "," + time + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 清空钱包记录
    public static void clearWallet() {
        String filepath = "data/" + currentUser.getUsername() + "/wallet.csv";
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("Amount,Category,Company,Time\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 保存愿望清单
    public static void saveWish(String name, String amount) {
        String filepath = "data/" + currentUser.getUsername() + "/wishlist.csv";
        try (FileWriter writer = new FileWriter(filepath, true)) {
            String time = getCurrentTime();
            writer.write(name + "," + amount + "," + time + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // utils/DataUtil.java
    public static void saveProfile(String username, String email, String password) {
        String oldUsername = currentUser.getUsername();
        File oldUserDir = new File("data/" + oldUsername);
        File newUserDir = new File("data/" + username);

        if (!newUserDir.exists()) {
            newUserDir.mkdirs(); // 创建新的用户目录
        }

        // 迁移文件
        if (oldUserDir.exists()) {
            File[] files = oldUserDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    Path sourcePath = file.toPath();
                    Path targetPath = Paths.get("data", username, file.getName());
                    try {
                        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 检查旧用户文件夹是否为空，若为空则删除
            File[] remainingFiles = oldUserDir.listFiles();
            if (remainingFiles == null || remainingFiles.length == 0) {
                oldUserDir.delete();
            }
        }

        try (FileWriter writer = new FileWriter("data/" + username + "/profile.csv")) {
            writer.write("Id,Email,Password,Username\n");
            writer.write(currentUser.getId() + "," + email + "," + password + "," + username + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 如果用户名发生变化，更新关联账户信息
        if (!oldUsername.equals(username)) {
            updateAssociatedAccounts(oldUsername, username);
        }
    }

    public static void deleteUserFiles(String username) {
        File userDir = new File("data/" + username);
        if (userDir.exists()) {
            deleteDirectory(userDir);
        }
    }

    private static void deleteDirectory(File directory) {
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
    // 读取用户信息
    public static User readProfile(String username) {
        String data = DataUtil.readData(username + "/profile.csv", true);
        if (data == null) return null;

        String[] split = data.split(",");
        if (split.length >= 4) { // 确保有足够的字段
            User user = new User(split[1], split[2], split[3]);
            user.setId(split[0]); // 设置 ID
            return user;
        } else {
            System.err.println("Invalid profile data for user: " + username);
            return null;
        }
    }

    // 创建用户及其相关文件
    public static boolean createUserAndFiles(String username, String email, String password) {
        File file = new File("data/" + username);
        if (!file.exists()) {
            file.mkdir();

            saveProfile(username, email, password);

            try (FileWriter writer = new FileWriter("data/" + username + "/wallet.csv")) {
                writer.write("Amount,Category,Company,Time\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (FileWriter writer = new FileWriter("data/" + username + "/wishlist.csv")) {
                writer.write("Wish,Amount,Time\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String filepath = "data/" + username + "/goal.txt";
            try (FileWriter writer = new FileWriter(filepath)) {
                writer.write("0");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        } else {
            return false;
        }
    }

    // 保存数据到指定文件
    public static void saveData(String filename, String data) {
        String filepath = "data/" + currentUser.getUsername() + "/" + filename;
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从指定文件读取数据
    public static String readData(String filename, boolean skipHeader) {
        File file = new File("data/" + filename);
        if (file.exists()) {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (skipHeader && lines.size() > 1) {
                return lines.get(1);
            } else if (!skipHeader && !lines.isEmpty()) {
                return lines.get(0);
            }
        }
        return null;
    }

    // 发送关联请求
    public static boolean sendAssociationRequest(String requesterUsername, String targetUsername) {
        if (requesterUsername == null || targetUsername == null) {
            return false;
        }

        // 检查是否已发送过请求
        List<String[]> existingRequests = readAssociationRequests(targetUsername);
        for (String[] request : existingRequests) {
            if (request[0].equals(requesterUsername)) {
                System.out.println("Requests have been sent:" + requesterUsername + " -> " + targetUsername);
                return false;
            }
        }

        // 检查是否已经关联
        Map<String, String> requesterAssociations = getAssociatedAccounts(requesterUsername);
        if (requesterAssociations.containsKey(targetUsername)) {
            System.out.println("Existing associated relationship:" + requesterUsername + " <-> " + targetUsername);
            return false;
        }

        String filepath = "data/" + targetUsername + "/association_requests.txt";
        try (FileWriter writer = new FileWriter(filepath, true)) {
            writer.write(requesterUsername + "," + getCurrentTime() + "\n");
            System.out.println("Association accessment have been sent: " + requesterUsername + " -> " + targetUsername);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 读取关联请求
    public static List<String[]> readAssociationRequests(String username) {
        if (username == null) {
            return new ArrayList<>();
        }
        List<String[]> requests = new ArrayList<>();
        String filepath = "data/" + username + "/association_requests.txt";
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        requests.add(parts);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return requests;
    }

    // 接受关联请求
    public static boolean acceptAssociationRequest(String requesterUsername, String targetUsername) {
        if (requesterUsername == null || targetUsername == null) {
            return false;
        }

        // 检查是否已经关联
        Map<String, String> requesterAssociations = getAssociatedAccounts(requesterUsername);
        if (requesterAssociations.containsKey(targetUsername)) {
            System.out.println("Existing associated relationship:" + requesterUsername + " <-> " + targetUsername);
            return false;
        }

        // 保存关联信息到双方文件
        saveAssociation(requesterUsername, targetUsername);
        saveAssociation(targetUsername, requesterUsername);

        // 删除请求记录
        removeAssociationRequest(requesterUsername, targetUsername);
        return true;
    }

    // 拒绝关联请求
    public static void rejectAssociationRequest(String requesterUsername, String targetUsername) {
        if (requesterUsername == null || targetUsername == null) {
            return;
        }
        removeAssociationRequest(requesterUsername, targetUsername);
    }

    // 保存关联信息
    private static void saveAssociation(String username, String associatedUsername) {
        if (username == null || associatedUsername == null) {
            return;
        }
        String dirPath = "data/" + username;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建目录
        }
        String filepath = dirPath + "/associated_accounts.csv";
        System.out.println("Attempting to save association to: " + filepath);
        try (FileWriter writer = new FileWriter(filepath, true)) {
            System.out.println("Saving association for user: " + username + ", associated with: " + associatedUsername);
            // 默认权限为部分查看
            writer.write(associatedUsername + ",Partial view," + getCurrentTime() + "\n");
            System.out.println("Association saved successfully for user: " + username);
        } catch (IOException e) {
            System.out.println("Failed to save association: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 删除关联请求记录
    private static void removeAssociationRequest(String requesterUsername, String targetUsername) {
        if (requesterUsername == null || targetUsername == null) {
            return;
        }
        String filepath = "data/" + targetUsername + "/association_requests.txt";
        List<String> lines = new ArrayList<>();
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(requesterUsername + ",")) {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (FileWriter writer = new FileWriter(filepath)) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
                System.out.println("Association request from " + requesterUsername + " removed for " + targetUsername);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 加载关联账号信息
    public static void loadAssociatedAccounts(User user) {
        user.getAssociatedAccounts().clear(); // 清除旧数据

        String filepath = "data/" + user.getUsername() + "/associated_accounts.csv";
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(",");
                    if (split.length >= 2) {
                        user.addAssociatedAccount(split[0], split[1]); // 只传递两个参数
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 设置关联账号权限
    public static boolean setAssociationPermission(String username, String associatedUsername, String permission) {
        if (username == null || associatedUsername == null || permission == null) {
            return false;
        }
        String filepath = "data/" + username + "/associated_accounts.csv";
        List<String> lines = new ArrayList<>();
        boolean found = false;
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(",");
                    if (split.length >= 2 && split[0].equals(associatedUsername)) {
                        // 保留时间戳
                        String timestamp = split.length >= 3 ? split[2] : getCurrentTime();
                        lines.add(associatedUsername + "," + "partial view" + "," + timestamp);
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (found) {
                try (FileWriter writer = new FileWriter(filepath)) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    // 获取关联账号信息（详细版）
    public static Map<String, String[]> getAssociatedAccountsWithDetails(String username) {
        Map<String, String[]> associatedAccounts = new LinkedHashMap<>();
        String filepath = "data/" + username + "/associated_accounts.csv";
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(",");
                    if (split.length >= 3) {
                        associatedAccounts.put(split[0], new String[]{split[1], split[2]});
                    } else if (split.length >= 2) {
                        associatedAccounts.put(split[0], new String[]{split[1], ""});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return associatedAccounts;
    }

    // 获取关联账号信息（简化版，只返回用户名和权限）
    public static Map<String, String> getAssociatedAccounts(String username) {
        Map<String, String> associatedAccounts = new LinkedHashMap<>();
        Map<String, String[]> detailedAccounts = getAssociatedAccountsWithDetails(username);
        for (Map.Entry<String, String[]> entry : detailedAccounts.entrySet()) {
            associatedAccounts.put(entry.getKey(), entry.getValue()[0]);
        }
        return associatedAccounts;
    }
    // 查找与指定用户关联的所有用户
    public static List<String> findAssociatedUsers(String username) {
        List<String> associatedUsers = new ArrayList<>();
        // 遍历所有用户的关联请求文件
        File dataDir = new File("data");
        File[] userDirs = dataDir.listFiles();
        if (userDirs != null) {
            for (File userDir : userDirs) {
                if (userDir.isDirectory()) {
                    String user = userDir.getName();
                    Map<String, String> associatedAccounts = getAssociatedAccounts(user);
                    if (associatedAccounts.containsKey(username)) {
                        associatedUsers.add(user);
                    }
                }
            }
        }
        return associatedUsers;
    }

    // 更新关联用户的关联账户信息
    public static void updateAssociatedAccounts(String oldUsername, String newUsername) {
        List<String> associatedUsers = findAssociatedUsers(oldUsername);
        for (String associatedUser : associatedUsers) {
            File associatedAccountsFile = new File("data/" + associatedUser + "/associated_accounts.csv");
            if (associatedAccountsFile.exists()) {
                try (FileReader reader = new FileReader(associatedAccountsFile);
                     java.io.BufferedReader br = new java.io.BufferedReader(reader)) {
                    List<String> lines = new ArrayList<>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith(oldUsername + ",")) {
                            line = newUsername + line.substring(oldUsername.length());
                        }
                        lines.add(line);
                    }
                    try (FileWriter writer = new FileWriter(associatedAccountsFile)) {
                        for (String updatedLine : lines) {
                            writer.write(updatedLine + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 解除关联
    public static boolean removeAssociation(String username, String associatedUsername) {
        if (username == null || associatedUsername == null) {
            return false;
        }
        String filepath = "data/" + username + "/associated_accounts.csv";
        List<String> lines = new ArrayList<>();
        boolean found = false;
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(",");
                    if (split.length >= 1 && split[0].equals(associatedUsername)) {
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (found) {
                try (FileWriter writer = new FileWriter(filepath)) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Successfully unassociating: " + username + " <-> " + associatedUsername);
                return true;
            }
        }
        System.out.println("No associated records were found: " + username + " <-> " + associatedUsername);
        return false;
    }

    // 读取钱包记录
    public static List<Wallet> readWallet() {
        String filepath = "data/" + currentUser.getUsername() + "/wallet.csv";

        List<Wallet> list = new ArrayList<>();
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    if (lineNumber > 0) {
                        String[] split = line.split(",");
                        if (split.length >= 4) {
                            double amount = Double.parseDouble(split[0]);
                            String category = split[1];
                            String company = split[2];
                            String time = split[3];
                            list.add(new Wallet(category, amount, company, time));
                        }
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // 读取愿望清单
    public static List<Wish> readWishList() {
        String filepath = "data/" + currentUser.getUsername() + "/wishlist.csv";

        List<Wish> list = new ArrayList<>();
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    if (lineNumber++ > 0) {
                        String[] split = line.split(",");
                        if (split.length >= 3) {
                            list.add(new Wish(split[0], Double.parseDouble(split[1]), split[2]));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // 读取收入记录
    public static List<Wallet> readIncome() {
        return readWallet().stream().filter(row -> row.getAmount() >= 0).collect(Collectors.toList());
    }

    // 读取支出记录
    public static List<Wallet> readExpense() {
        return readWallet().stream().filter(row -> row.getAmount() < 0).collect(Collectors.toList());
    }

    // 解除双方的关联关系
    public static boolean disassociateAccount(String username, String associatedUsername) {
        if (username == null || associatedUsername == null) {
            return false;
        }

        // 移除双方的关联信息
        boolean removedFromUser = removeAssociation(username, associatedUsername);
        boolean removedFromAssociated = removeAssociation(associatedUsername, username);

        return removedFromUser && removedFromAssociated;
    }
}