package strip.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import strip.domain.enumeration.TripStatus;

/**
 * A DTO for the {@link strip.domain.Trip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripDTO implements Serializable {

    private Long id;

    private UUID tripID;

    @Lob
    private byte[] tripImg;

    private String tripImgContentType;

    private Double pricePerSeat;

    private Integer maxSeat;

    private Integer totalTime;

    private Double totalDistance;

    private Instant startDate;

    private Instant endDate;

    private Integer currentSeat;

    private String startLocation;

    private String endLocation;

    private String description;

    private String condition;

    private String cancelReason;

    private TripStatus tripStatus;

    private VehicleDTO vehicle;

    private DriverDTO driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTripID() {
        return tripID;
    }

    public void setTripID(UUID tripID) {
        this.tripID = tripID;
    }

    public byte[] getTripImg() {
        return tripImg;
    }

    public void setTripImg(byte[] tripImg) {
        this.tripImg = tripImg;
    }

    public String getTripImgContentType() {
        return tripImgContentType;
    }

    public void setTripImgContentType(String tripImgContentType) {
        this.tripImgContentType = tripImgContentType;
    }

    public Double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public Integer getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(Integer maxSeat) {
        this.maxSeat = maxSeat;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(Integer currentSeat) {
        this.currentSeat = currentSeat;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripDTO)) {
            return false;
        }

        TripDTO tripDTO = (TripDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripDTO{" +
            "id=" + getId() +
            ", tripID='" + getTripID() + "'" +
            ", tripImg='" + getTripImg() + "'" +
            ", pricePerSeat=" + getPricePerSeat() +
            ", maxSeat=" + getMaxSeat() +
            ", totalTime=" + getTotalTime() +
            ", totalDistance=" + getTotalDistance() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", currentSeat=" + getCurrentSeat() +
            ", startLocation='" + getStartLocation() + "'" +
            ", endLocation='" + getEndLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", condition='" + getCondition() + "'" +
            ", cancelReason='" + getCancelReason() + "'" +
            ", tripStatus='" + getTripStatus() + "'" +
            ", vehicle=" + getVehicle() +
            ", driver=" + getDriver() +
            "}";
    }
}
