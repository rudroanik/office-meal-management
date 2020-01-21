package com.example.mealcounter.Utils;

public class UserInfo {

    private String userId;
    private String userName;
    private String role;

    public UserInfo() {
    }

    public UserInfo(String userId, String userName, String role) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }
}
