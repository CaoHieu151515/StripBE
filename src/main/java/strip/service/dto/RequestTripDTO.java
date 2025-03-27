package strip.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.PassengerType;

/**
 * A DTO for the {@link strip.domain.RequestTrip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestTripDTO implements Serializable {

    private Long id;

    private UUID requestTripID;

    private String startLoca;

    private String endLoca;

    private Double amountApproveFee;

    private Integer numberofSeats;

    @Lob
    private byte[] luggageImg;

    private String luggageImgContentType;

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

    private TripDTO trip;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestTripID() {
        return requestTripID;
    }

    public void setRequestTripID(UUID requestTripID) {
        this.requestTripID = requestTripID;
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

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestTripDTO)) {
            return false;
        }

        RequestTripDTO requestTripDTO = (RequestTripDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, requestTripDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestTripDTO{" +
            "id=" + getId() +
            ", requestTripID='" + getRequestTripID() + "'" +
            ", startLoca='" + getStartLoca() + "'" +
            ", endLoca='" + getEndLoca() + "'" +
            ", amountApproveFee=" + getAmountApproveFee() +
            ", numberofSeats=" + getNumberofSeats() +
            ", luggageImg='" + getLuggageImg() + "'" +
            ", luggageDescription='" + getLuggageDescription() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", pickUpTime='" + getPickUpTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", checkIn='" + getCheckIn() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOut='" + getCheckOut() + "'" +
            ", checkOutTIme='" + getCheckOutTIme() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            ", trip=" + getTrip() +
            ", user=" + getUser() +
            "}";
    }
}
