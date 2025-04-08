package strip.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link strip.domain.TripStopLocation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripStopLocationDTO implements Serializable {

    private Long id;

    private UUID stopLocaID;

    private String stopLoca;

    private Integer stoplocaPosition;

    private Integer estimatedTime;

    private Double estimatedKM;

    private Instant stopLocaTime;

    private String stopLocaStatus;

    private TripDTO trip;

    public TripStopLocationDTO() {}

    public TripStopLocationDTO(
        Long id,
        UUID stopLocaID,
        String stopLoca,
        Integer stoplocaPosition,
        Integer estimatedTime,
        Double estimatedKM,
        Instant stopLocaTime,
        String stopLocaStatus,
        TripDTO trip
    ) {
        this.id = id;
        this.stopLocaID = stopLocaID;
        this.stopLoca = stopLoca;
        this.stoplocaPosition = stoplocaPosition;
        this.estimatedTime = estimatedTime;
        this.estimatedKM = estimatedKM;
        this.stopLocaTime = stopLocaTime;
        this.stopLocaStatus = stopLocaStatus;
        this.trip = trip;
    }

    public TripStopLocationDTO(
        UUID stopLocaID,
        String stopLoca,
        Integer stoplocaPosition,
        Integer estimatedTime,
        Double estimatedKM,
        Instant stopLocaTime,
        String stopLocaStatus
    ) {
        this.stopLocaID = stopLocaID;
        this.stopLoca = stopLoca;
        this.stoplocaPosition = stoplocaPosition;
        this.estimatedTime = estimatedTime;
        this.estimatedKM = estimatedKM;
        this.stopLocaTime = stopLocaTime;
        this.stopLocaStatus = stopLocaStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripStopLocationDTO)) {
            return false;
        }

        TripStopLocationDTO tripStopLocationDTO = (TripStopLocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripStopLocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripStopLocationDTO{" +
                "id=" + getId() +
                ", stopLocaID='" + getStopLocaID() + "'" +
                ", stopLoca='" + getStopLoca() + "'" +
                ", stoplocaPosition=" + getStoplocaPosition() +
                ", estimatedTime=" + getEstimatedTime() +
                ", estimatedKM=" + getEstimatedKM() +
                ", stopLocaTime='" + getStopLocaTime() + "'" +
                ", stopLocaStatus='" + getStopLocaStatus() + "'" +
                ", trip=" + getTrip() +
                "}";
    }
}
