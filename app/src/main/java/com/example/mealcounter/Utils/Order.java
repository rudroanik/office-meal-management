package com.example.mealcounter.Utils;

public class Order {
    private String userId;
    private String orderId;
    private String userName;
    private String date;
    private boolean isOrdered;
    private long time;

    public Order() {
    }

    public Order(String name, String date, boolean isOrdered, long time) {
        this.userName = name;
        this.date = date;
        this.isOrdered = isOrdered;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public long getTime() {
        return time;
    }
}
