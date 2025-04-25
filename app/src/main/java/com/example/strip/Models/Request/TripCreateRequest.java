package com.example.strip.Models.Request;

public class TripCreateRequest {
    private String driverId;
    private String vehicleId;
    private byte[] tripImg;
    private String tripImgContentType;
    private int pricePerSeat;
    private int currentSeat;
    private int maxSeat;
    private String startDate;
    private String endDate;
    private String startLocation;
    private String endLocation;
    private String description;
    private String condition;

    public TripCreateRequest(String driverId,
                             String vehicleId,
                             byte[] tripImg,
                             String tripImgContentType,
                             int pricePerSeat,
                             int currentSeat,
                             int maxSeat,
                             String endDate,
                             String startDate,
                             String startLocation,
                             String endLocation,
                             String description,
                             String condition) {
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.tripImg = tripImg;
        this.tripImgContentType = tripImgContentType;
        this.pricePerSeat = pricePerSeat;
        this.currentSeat = currentSeat;
        this.maxSeat = maxSeat;
        this.endDate = endDate;
        this.startDate = startDate;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.description = description;
        this.condition = condition;
    }

    public int getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(int pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public byte[] getTripImg() {
        return tripImg;
    }

    public void setTripImg(byte[] tripImg) {
        this.tripImg = tripImg;
    }

    public String getTripImgContentType() {
        return tripImgContentType;
    }

    public void setTripImgContentType(String tripImgContentType) {
        this.tripImgContentType = tripImgContentType;
    }

    public int getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(int currentSeat) {
        this.currentSeat = currentSeat;
    }
}
