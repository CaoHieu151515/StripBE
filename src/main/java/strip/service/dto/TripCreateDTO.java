package strip.service.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class TripCreateDTO {

    private UUID tripID;
    private UUID driverId;
    private byte[] tripImg;
    private String tripImgContentType;
    private Double pricePerSeat;
    private Integer maxSeat;
    private Instant startDate;
    private Instant endDate;
    private Integer currentSeat;
    private String startLocation;
    private String endLocation;
    private String description;
    private String condition;
    private Set<TripStopLocationDTO> stopLocations;
    private UUID vehicleId;

    private Double totalDistance;

    public UUID getTripID() {
        return tripID;
    }

    public void setTripID(UUID tripID) {
        this.tripID = tripID;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
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

    public Set<TripStopLocationDTO> getStopLocations() {
        return stopLocations;
    }

    public void setStopLocations(Set<TripStopLocationDTO> stopLocations) {
        this.stopLocations = stopLocations;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
