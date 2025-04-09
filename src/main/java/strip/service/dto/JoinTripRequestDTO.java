package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.PassengerType;

public class JoinTripRequestDTO {

    private UUID tripId;
    private int numberOfSeats;
    private PassengerType type;
    private String luggageDescription;
    private Instant pickUpTime;
    private Double amountApproveFee;
    private byte[] luggageImg;
    private String luggageImgContentType;
    private UUID startLoca;
    private UUID endLoca;

    private boolean payNow; // true = trả trước, false = trả sau

    // === Getters and Setters ===

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public PassengerType getType() {
        return type;
    }

    public void setType(PassengerType type) {
        this.type = type;
    }

    public String getLuggageDescription() {
        return luggageDescription;
    }

    public void setLuggageDescription(String luggageDescription) {
        this.luggageDescription = luggageDescription;
    }

    public Instant getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Instant pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public byte[] getLuggageImg() {
        return luggageImg;
    }

    public void setLuggageImg(byte[] luggageImg) {
        this.luggageImg = luggageImg;
    }

    public String getLuggageImgContentType() {
        return luggageImgContentType;
    }

    public void setLuggageImgContentType(String luggageImgContentType) {
        this.luggageImgContentType = luggageImgContentType;
    }

    public boolean isPayNow() {
        return payNow;
    }

    public void setPayNow(boolean payNow) {
        this.payNow = payNow;
    }

    public Double getAmountApproveFee() {
        return amountApproveFee;
    }

    public void setAmountApproveFee(Double amountApproveFee) {
        this.amountApproveFee = amountApproveFee;
    }

    public UUID getStartLoca() {
        return startLoca;
    }

    public void setStartLoca(UUID startLoca) {
        this.startLoca = startLoca;
    }

    public UUID getEndLoca() {
        return endLoca;
    }

    public void setEndLoca(UUID endLoca) {
        this.endLoca = endLoca;
    }
}
