package strip.service.dto;

import java.time.Instant;

public class TripUpdateDTO {

    private Double pricePerSeat;
    private Integer maxSeat;
    private Instant startDate;
    private Instant endDate;
    private String startLocation;
    private String endLocation;
    private String description;
    private String condition;

    public TripUpdateDTO() {}

    public TripUpdateDTO(
        Double pricePerSeat,
        Integer maxSeat,
        Instant startDate,
        Instant endDate,
        String startLocation,
        String endLocation,
        String description,
        String condition
    ) {
        this.pricePerSeat = pricePerSeat;
        this.maxSeat = maxSeat;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.description = description;
        this.condition = condition;
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
}
