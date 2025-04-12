package com.example.strip.Models.Request;

public class JoinTripRequest {
    private String tripId;
    private int numberOfSeats;
    private String type;
    private String luggageDescription;
    private String pickUpTime;
    private int amountApproveFee;
    private String startLoca;
    private String endLoca;
    private boolean payNow;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getLuggageDescription() {
        return luggageDescription;
    }

    public void setLuggageDescription(String luggageDescription) {
        this.luggageDescription = luggageDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public int getAmountApproveFee() {
        return amountApproveFee;
    }

    public void setAmountApproveFee(int amountApproveFee) {
        this.amountApproveFee = amountApproveFee;
    }

    public String getStartLoca() {
        return startLoca;
    }

    public void setStartLoca(String startLoca) {
        this.startLoca = startLoca;
    }

    public String getEndLoca() {
        return endLoca;
    }

    public void setEndLoca(String endLoca) {
        this.endLoca = endLoca;
    }

    public boolean isPayNow() {
        return payNow;
    }

    public void setPayNow(boolean payNow) {
        this.payNow = payNow;
    }
}
