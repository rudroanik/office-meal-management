package com.example.mealcounter.Utils;

import java.util.List;

public class MonthlyInfo {
    private String userName;
    private String userId;
    private int totalOrder;


    public MonthlyInfo(String userName, String userId, int totalOrder) {
        this.userName = userName;
        this.userId = userId;
        this.totalOrder = totalOrder;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public int getTotalOrder() {
        return totalOrder;
    }
}
