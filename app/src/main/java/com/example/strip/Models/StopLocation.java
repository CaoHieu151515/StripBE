package com.example.strip.Models;

public class StopLocation {
    private int id;
    private String stopLocaID;
    private String stopLoca;
    private String stopLocaTime;
    private String stopLocaStatus;

    private int tripPositon;
    private int estimatedTime;
    private double estimatedKM;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStopLocaID() {
        return stopLocaID;
    }

    public void setStopLocaID(String stopLocaID) {
        this.stopLocaID = stopLocaID;
    }

    public String getStopLoca() {
        return stopLoca;
    }

    public void setStopLoca(String stopLoca) {
        this.stopLoca = stopLoca;
    }

    public String getStopLocaTime() {
        return stopLocaTime;
    }

    public void setStopLocaTime(String stopLocaTime) {
        this.stopLocaTime = stopLocaTime;
    }

    public String getStopLocaStatus() {
        return stopLocaStatus;
    }

    public void setStopLocaStatus(String stopLocaStatus) {
        this.stopLocaStatus = stopLocaStatus;
    }

    public int getTripPositon() {
        return tripPositon;
    }

    public void setTripPositon(int tripPositon) {
        this.tripPositon = tripPositon;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public double getEstimatedKM() {
        return estimatedKM;
    }

    public void setEstimatedKM(double estimatedKM) {
        this.estimatedKM = estimatedKM;
    }
}
