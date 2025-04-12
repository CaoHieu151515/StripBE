package com.example.strip.Models;

import com.google.gson.annotations.SerializedName;

public class Trip {
    @SerializedName("tripID")
    private String tripID;

    @SerializedName("startLocation")
    private String startLocation;

    @SerializedName("endLocation")
    private String endLocation;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("pricePerSeat")
    private double pricePerSeat;

    @SerializedName("currentSeat")
    private int currentSeat;

    @SerializedName("maxSeat")
    private int maxSeat;

    @SerializedName("tripImgUrl")
    private String tripImgUrl;

    @SerializedName("driverName")
    private String driverName;

    @SerializedName("vehicleType")
    private String vehicleType;

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public int getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(int currentSeat) {
        this.currentSeat = currentSeat;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
