package com.example.strip.Models;

public class Transaction {
    private int id;
    private String transID;
    private double amount;
    private String date;
    private String walletType;
    private String transStatus;

    public Transaction(int id, String transID, double amount, String walletType, String date, String transStatus) {
        this.id = id;
        this.transID = transID;
        this.amount = amount;
        this.walletType = walletType;
        this.date = date;
        this.transStatus = transStatus;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }
}
