package com.example.strip.Models.Response;

import com.google.gson.annotations.SerializedName;

public class TripActiveResponse {
    @SerializedName("stripID")
    private String stripID;

    @SerializedName("startDay")
    private String startDay;

    @SerializedName("endDay")
    private String endDay;

    @SerializedName("startLocation")
    private String startLocation;

    @SerializedName("endlocation")
    private String endlocation;

    @SerializedName("price")
    private double price;

    @SerializedName("status")
    private String status;

    @SerializedName("totalTime")
    private int totalTime;

    public String getStripID() {
        return stripID;
    }

    public void setStripID(String stripID) {
        this.stripID = stripID;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndlocation() {
        return endlocation;
    }

    public void setEndlocation(String endlocation) {
        this.endlocation = endlocation;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
    // Getters and Setters (or use Lombok)
}
