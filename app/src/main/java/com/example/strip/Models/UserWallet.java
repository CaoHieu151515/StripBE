package com.example.strip.Models;

public class UserWallet {
    private int id;
    private String userWallet;
    private double before;
    private double amount;
    private double current;
    private String mobifyDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(String userWallet) {
        this.userWallet = userWallet;
    }

    public double getBefore() {
        return before;
    }

    public void setBefore(double before) {
        this.before = before;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public String getMobifyDate() {
        return mobifyDate;
    }

    public void setMobifyDate(String mobifyDate) {
        this.mobifyDate = mobifyDate;
    }
}
