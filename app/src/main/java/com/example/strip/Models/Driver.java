package com.example.strip.Models;

public class Driver {
    private int id;
    private String driverId;
    private boolean usedtoDriver;
    private String expirationDate;
    private String driverStatus;
    private int driverPoint;
    private String bannedDay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverID() {
        return driverId;
    }

    public void setDriverID(String driverId) {
        this.driverId = driverId;
    }

    public boolean isUsedtoDriver() {
        return usedtoDriver;
    }

    public void setUsedtoDriver(boolean usedtoDriver) {
        this.usedtoDriver = usedtoDriver;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getDriverPoint() {
        return driverPoint;
    }

    public void setDriverPoint(int driverPoint) {
        this.driverPoint = driverPoint;
    }

    public String getBannedDay() {
        return bannedDay;
    }

    public void setBannedDay(String bannedDay) {
        this.bannedDay = bannedDay;
    }
}
