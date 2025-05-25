package model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    private String email;
    private String password;
    private String username;
    private Map<String, String> associatedAccounts = new HashMap<>();
    private String id;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.id = UUID.randomUUID().toString(); // 生成唯一 ID
    }

    // Getters and setters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }

    public void addAssociatedAccount(String username, String permission) {
        associatedAccounts.put(username, permission);
    }

    public Map<String, String> getAssociatedAccounts() {
        return associatedAccounts;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}