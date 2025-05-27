package com.example.strip.Models.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripBookingResponse {
    @SerializedName("tripID")
    public String tripID;

    @SerializedName("startLocation")
    public String startLocation;

    @SerializedName("endLocation")
    public String endLocation;

    @SerializedName("description")
    public String description;

    @SerializedName("condition")
    public String condition;

    @SerializedName("startDate")
    public String startDate;

    @SerializedName("endDate")
    public String endDate;

    @SerializedName("pricePerSeat")
    public int pricePerSeat;

    @SerializedName("maxSeat")
    public int maxSeat;

    @SerializedName("currentSeat")
    public int currentSeat;

    @SerializedName("tripStatus")
    public String tripStatus;

    @SerializedName("tripImgUrl")
    public String tripImgUrl;

    @SerializedName("driverName")
    public String driverName;

    @SerializedName("driverPhone")
    public String driverPhone;

    @SerializedName("vehicleType")
    public String vehicleType;

    @SerializedName("vehicleNumber")
    public String vehicleNumber;

    @SerializedName("vehicleColor")
    public String vehicleColor;

    @SerializedName("vehicleBrand")
    public String vehicleBrand;

    @SerializedName("vehicleImageUrl")
    public String vehicleImageUrl;
    @SerializedName("tripHandleID")
    public String tripHandleID;
    @SerializedName("stopLocations")
    public List<StopLocationBookingResponse> stopLocationBookingResponseList;
    @SerializedName("driverID")
    public String driverID;

    public String getDriverID() {
        return driverID;
    }
    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }
    public String getTripHandleID() {
        return tripHandleID;
    }

    public void setTripHandleID(String tripHandleID) {
        this.tripHandleID = tripHandleID;
    }

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

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(int pricePerSeat) {
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

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public List<StopLocationBookingResponse> getStopLocationBookingResponseList() {
        return stopLocationBookingResponseList;
    }

    public void setStopLocationBookingResponseList(List<StopLocationBookingResponse> stopLocationBookingResponseList) {
        this.stopLocationBookingResponseList = stopLocationBookingResponseList;
    }
}
