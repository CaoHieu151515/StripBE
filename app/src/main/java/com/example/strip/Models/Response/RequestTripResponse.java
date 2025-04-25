package com.example.strip.Models.Response;

import com.example.strip.Models.StopLocationOfRequestTrip;

public class RequestTripResponse {
    public String requestTripID;
    public StopLocationOfRequestTrip startLoca;
    public StopLocationOfRequestTrip endLoca;
    public int amountApproveFee;
    public int numberofSeats;
    public String luggageDescription;
    public String type;
    public String status;
    public String pickUpTime;
    public String endTime;
    public boolean checkIn;
    public String checkInTime;
    public boolean checkOut;
    public String checkOutTIme;
    public String appliedAt;

    public String getRequestTripID() {
        return requestTripID;
    }

    public void setRequestTripID(String requestTripID) {
        this.requestTripID = requestTripID;
    }

    public StopLocationOfRequestTrip getEndLoca() {
        return endLoca;
    }

    public void setEndLoca(StopLocationOfRequestTrip endLoca) {
        this.endLoca = endLoca;
    }

    public StopLocationOfRequestTrip getStartLoca() {
        return startLoca;
    }

    public void setStartLoca(StopLocationOfRequestTrip startLoca) {
        this.startLoca = startLoca;
    }

    public int getNumberofSeats() {
        return numberofSeats;
    }

    public void setNumberofSeats(int numberofSeats) {
        this.numberofSeats = numberofSeats;
    }

    public int getAmountApproveFee() {
        return amountApproveFee;
    }

    public void setAmountApproveFee(int amountApproveFee) {
        this.amountApproveFee = amountApproveFee;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public boolean isCheckOut() {
        return checkOut;
    }

    public void setCheckOut(boolean checkOut) {
        this.checkOut = checkOut;
    }

    public String getCheckOutTIme() {
        return checkOutTIme;
    }

    public void setCheckOutTIme(String checkOutTIme) {
        this.checkOutTIme = checkOutTIme;
    }

    public String getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }
}
