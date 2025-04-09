package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.PassengerType;

public class RequestTripCusDTO {

    private UUID requestTripID;
    private TripStopLocationSkipTripDTO startLoca;
    private TripStopLocationSkipTripDTO endLoca;
    private Double amountApproveFee;
    private Integer numberofSeats;

    // ✅ Ảnh hành lý sẽ trả về dưới dạng URL
    private String luggageImgUrl;

    private String luggageDescription;
    private PassengerType type;
    private PassengerStatus status;

    private Instant pickUpTime;
    private Instant endTime;
    private Boolean checkIn;
    private Instant checkInTime;
    private Boolean checkOut;
    private Instant checkOutTIme;
    private Instant appliedAt;

    // --- Getters & Setters ---

    public UUID getRequestTripID() {
        return requestTripID;
    }

    public void setRequestTripID(UUID requestTripID) {
        this.requestTripID = requestTripID;
    }

    public TripStopLocationSkipTripDTO getStartLoca() {
        return startLoca;
    }

    public void setStartLoca(TripStopLocationSkipTripDTO startLoca) {
        this.startLoca = startLoca;
    }

    public TripStopLocationSkipTripDTO getEndLoca() {
        return endLoca;
    }

    public void setEndLoca(TripStopLocationSkipTripDTO endLoca) {
        this.endLoca = endLoca;
    }

    public Double getAmountApproveFee() {
        return amountApproveFee;
    }

    public void setAmountApproveFee(Double amountApproveFee) {
        this.amountApproveFee = amountApproveFee;
    }

    public Integer getNumberofSeats() {
        return numberofSeats;
    }

    public void setNumberofSeats(Integer numberofSeats) {
        this.numberofSeats = numberofSeats;
    }

    public String getLuggageImgUrl() {
        return luggageImgUrl;
    }

    public void setLuggageImgUrl(String luggageImgUrl) {
        this.luggageImgUrl = luggageImgUrl;
    }

    public String getLuggageDescription() {
        return luggageDescription;
    }

    public void setLuggageDescription(String luggageDescription) {
        this.luggageDescription = luggageDescription;
    }

    public PassengerType getType() {
        return type;
    }

    public void setType(PassengerType type) {
        this.type = type;
    }

    public PassengerStatus getStatus() {
        return status;
    }

    public void setStatus(PassengerStatus status) {
        this.status = status;
    }

    public Instant getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Instant pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Boolean checkIn) {
        this.checkIn = checkIn;
    }

    public Instant getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Instant checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Boolean getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Boolean checkOut) {
        this.checkOut = checkOut;
    }

    public Instant getCheckOutTIme() {
        return checkOutTIme;
    }

    public void setCheckOutTIme(Instant checkOutTIme) {
        this.checkOutTIme = checkOutTIme;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }
}
