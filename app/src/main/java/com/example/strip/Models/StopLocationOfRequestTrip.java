package com.example.strip.Models;

public class StopLocationOfRequestTrip {
    public String stopLocaID;
    public String stopLoca;
    public int tripPositon;
    public String stopLocaTime;
    public int estimatedTime;
    public double estimatedKM;
    public String stopLocaStatus;

    public String getStopLoca() {
        return stopLoca;
    }

    public void setStopLoca(String stopLoca) {
        this.stopLoca = stopLoca;
    }

    public String getStopLocaID() {
        return stopLocaID;
    }

    public void setStopLocaID(String stopLocaID) {
        this.stopLocaID = stopLocaID;
    }

    public int getTripPositon() {
        return tripPositon;
    }

    public void setTripPositon(int tripPositon) {
        this.tripPositon = tripPositon;
    }

    public String getStopLocaTime() {
        return stopLocaTime;
    }

    public void setStopLocaTime(String stopLocaTime) {
        this.stopLocaTime = stopLocaTime;
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

    public String getStopLocaStatus() {
        return stopLocaStatus;
    }

    public void setStopLocaStatus(String stopLocaStatus) {
        this.stopLocaStatus = stopLocaStatus;
    }
}
