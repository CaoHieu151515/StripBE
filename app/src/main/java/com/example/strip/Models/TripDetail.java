package com.example.strip.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripDetail {
    private String tripID;
    private String startLocation;
    private String endLocation;
    private String description;
    private String condition;
    private String startDate;
    private String endDate;
    private double pricePerSeat;
    private int maxSeat;
    private int currentSeat;
    private String tripStatus;
    private String cancelReason;
    private String tripImgUrl;
    private int totalTime;
    private int totalDistance;
    @SerializedName("driver")
    private DriverOfTripDetail driverOfTripDetail;
    @SerializedName("vehicle")
    private VehicleOfTripDetail vehicleOfTripDetail;
    @SerializedName("stoplocation")

    private List<StopLocation> stopLocations;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public int getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(int currentSeat) {
        this.currentSeat = currentSeat;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public List<StopLocation> getStopLocations() {
        return stopLocations;
    }

    public void setStopLocations(List<StopLocation> stopLocations) {
        this.stopLocations = stopLocations;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public DriverOfTripDetail getDriverOfTripDetail() {
        return driverOfTripDetail;
    }

    public void setDriverOfTripDetail(DriverOfTripDetail driverOfTripDetail) {
        this.driverOfTripDetail = driverOfTripDetail;
    }

    public VehicleOfTripDetail getVehicleOfTripDetail() {
        return vehicleOfTripDetail;
    }

    public void setVehicleOfTripDetail(VehicleOfTripDetail vehicleOfTripDetail) {
        this.vehicleOfTripDetail = vehicleOfTripDetail;
    }
}
