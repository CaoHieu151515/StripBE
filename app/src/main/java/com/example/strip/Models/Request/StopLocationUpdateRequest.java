package com.example.strip.Models.Request;

public class StopLocationUpdateRequest {
    private String stopLoca;
    private String stopLocaTime;
    private String stopLocaStatus;
    private int estimatedTime;
    private double estimatedKM;
    private int stoplocaPosition;

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

    public int getStoplocaPosition() {
        return stoplocaPosition;
    }

    public void setStoplocaPosition(int stoplocaPosition) {
        this.stoplocaPosition = stoplocaPosition;
    }
}
