package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.TripStatus;

public class TripListDTO {

    UUID stripID;
    Instant startDay;
    Instant endDay;
    String startLocation;
    String endlocation;
    Double price;
    TripStatus status;
    int totalTime;
    private String tripHandleId;

    public UUID getStripID() {
        return stripID;
    }

    public void setStripID(UUID stripID) {
        this.stripID = stripID;
    }

    public Instant getStartDay() {
        return startDay;
    }

    public void setStartDay(Instant startDay) {
        this.startDay = startDay;
    }

    public Instant getEndDay() {
        return endDay;
    }

    public void setEndDay(Instant endDay) {
        this.endDay = endDay;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndlocation() {
        return endlocation;
    }

    public void setEndlocation(String endlocation) {
        this.endlocation = endlocation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getTripHandleId() {
        return tripHandleId;
    }

    public void setTripHandleId(String tripHandleId) {
        this.tripHandleId = tripHandleId;
    }
}
