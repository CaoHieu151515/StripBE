package com.example.strip.Models.Response;

import com.google.gson.annotations.SerializedName;

public class StopLocationBookingResponse {
    @SerializedName("stopLocaID")
    public String stopLocaID;

    @SerializedName("stopLoca")
    public String stopLoca;

    @SerializedName("stoplocaPosition")
    public int stoplocaPosition;

    @SerializedName("estimatedTime")
    public int estimatedTime;

    @SerializedName("estimatedKM")
    public double estimatedKM;

    @SerializedName("stopLocaTime")
    public String stopLocaTime;

    @SerializedName("stopLocaStatus")
    public String stopLocaStatus;

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

    public int getStoplocaPosition() {
        return stoplocaPosition;
    }

    public void setStoplocaPosition(int stoplocaPosition) {
        this.stoplocaPosition = stoplocaPosition;
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
}
