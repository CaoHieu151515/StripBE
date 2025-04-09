package strip.service.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import strip.domain.enumeration.TripStatus;

public class TripDetailDTO {

    private UUID tripID;
    private String startLocation;
    private String endLocation;
    private String description;
    private String condition;
    private Instant startDate;
    private Instant endDate;
    private double pricePerSeat;
    private int maxSeat;
    private int currentSeat;
    private TripStatus tripStatus;
    private String cancelReason;
    private int totalTime;
    private double totalDistance;
    private String tripImgUrl;
    private DriverRawDTO driver;
    private VehicleRawDTO vehicle;
    private Set<TripStopLocationSkipTripDTO> stoplocation;

    public UUID getTripID() {
        return tripID;
    }

    public void setTripID(UUID tripID) {
        this.tripID = tripID;
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

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public int getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(int currentSeat) {
        this.currentSeat = currentSeat;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public DriverRawDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverRawDTO driver) {
        this.driver = driver;
    }

    public VehicleRawDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleRawDTO vehicle) {
        this.vehicle = vehicle;
    }

    public Set<TripStopLocationSkipTripDTO> getStoplocation() {
        return stoplocation;
    }

    public void setStoplocation(Set<TripStopLocationSkipTripDTO> stoplocation) {
        this.stoplocation = stoplocation;
    }
}
