package strip.service.dto;

import java.time.Instant;
import java.util.UUID;

public class TripStopLocationSkipTripDTO {

    private UUID stopLocaID;
    private String stopLoca;
    private Integer tripPositon;
    private Instant stopLocaTime;
    private Integer estimatedTime;
    private Double estimatedKM;
    private String stopLocaStatus;

    public UUID getStopLocaID() {
        return stopLocaID;
    }

    public void setStopLocaID(UUID stopLocaID) {
        this.stopLocaID = stopLocaID;
    }

    public String getStopLoca() {
        return stopLoca;
    }

    public void setStopLoca(String stopLoca) {
        this.stopLoca = stopLoca;
    }

    public Integer getTripPositon() {
        return tripPositon;
    }

    public void setTripPositon(Integer tripPositon) {
        this.tripPositon = tripPositon;
    }

    public Instant getStopLocaTime() {
        return stopLocaTime;
    }

    public void setStopLocaTime(Instant stopLocaTime) {
        this.stopLocaTime = stopLocaTime;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Double getEstimatedKM() {
        return estimatedKM;
    }

    public void setEstimatedKM(Double estimatedKM) {
        this.estimatedKM = estimatedKM;
    }

    public String getStopLocaStatus() {
        return stopLocaStatus;
    }

    public void setStopLocaStatus(String stopLocaStatus) {
        this.stopLocaStatus = stopLocaStatus;
    }
}
