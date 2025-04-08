package strip.service.dto;

import java.util.UUID;
import strip.domain.enumeration.PassengerType;

public class JoinTripRequestDTO {

    private UUID tripId;
    private int numberOfSeats;
    private Double amount;
    private boolean payNow;
    private String luggageDescription;
    private byte[] luggageImg;
    private String luggageImgType;

    private PassengerType type;

    private int startLocaPosition;
    private int endLocaPosition;

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

    public boolean isPayNow() {
        return payNow;
    }

    public void setPayNow(boolean payNow) {
        this.payNow = payNow;
    }

    public String getLuggageDescription() {
        return luggageDescription;
    }

    public void setLuggageDescription(String luggageDescription) {
        this.luggageDescription = luggageDescription;
    }

    public byte[] getLuggageImg() {
        return luggageImg;
    }

    public void setLuggageImg(byte[] luggageImg) {
        this.luggageImg = luggageImg;
    }

    public PassengerType getType() {
        return type;
    }

    public void setType(PassengerType type) {
        this.type = type;
    }

    public int getStartLocaPosition() {
        return startLocaPosition;
    }

    public void setStartLocaPosition(int startLocaPosition) {
        this.startLocaPosition = startLocaPosition;
    }

    public int getEndLocaPosition() {
        return endLocaPosition;
    }

    public void setEndLocaPosition(int endLocaPosition) {
        this.endLocaPosition = endLocaPosition;
    }

    public String getLuggageImgType() {
        return luggageImgType;
    }

    public void setLuggageImgType(String luggageImgType) {
        this.luggageImgType = luggageImgType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
