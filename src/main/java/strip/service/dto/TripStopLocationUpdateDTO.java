package strip.service.dto;

import java.time.Instant;
import java.util.UUID;

public class TripStopLocationUpdateDTO {

    private UUID triplocaUUID;
    private String stopLoca;
    private Instant stopLocaTime;
    private String stopLocaStatus;
    private Integer stoplocaPosition;
    private Integer estimatedTime;
    private Double estimatedKM;

    public TripStopLocationUpdateDTO() {}

    public TripStopLocationUpdateDTO(
        UUID triplocaUUID,
        String stopLoca,
        Instant stopLocaTime,
        String stopLocaStatus,
        Integer stoplocaPosition,
        Integer estimatedTime,
        Double estimatedKM
    ) {
        this.triplocaUUID = triplocaUUID;
        this.stopLoca = stopLoca;
        this.stopLocaTime = stopLocaTime;
        this.stopLocaStatus = stopLocaStatus;
        this.stoplocaPosition = stoplocaPosition;
        this.estimatedTime = estimatedTime;
        this.estimatedKM = estimatedKM;
    }

    public UUID getTriplocaUUID() {
        return triplocaUUID;
    }

    public void setTriplocaUUID(UUID triplocaUUID) {
        this.triplocaUUID = triplocaUUID;
    }

    public String getStopLoca() {
        return stopLoca;
    }

    public void setStopLoca(String stopLoca) {
        this.stopLoca = stopLoca;
    }

    public Instant getStopLocaTime() {
        return stopLocaTime;
    }

    public void setStopLocaTime(Instant stopLocaTime) {
        this.stopLocaTime = stopLocaTime;
    }

    public String getStopLocaStatus() {
        return stopLocaStatus;
    }

    public void setStopLocaStatus(String stopLocaStatus) {
        this.stopLocaStatus = stopLocaStatus;
    }

    public Integer getStoplocaPosition() {
        return stoplocaPosition;
    }

    public void setStoplocaPosition(Integer stoplocaPosition) {
        this.stoplocaPosition = stoplocaPosition;
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
}
