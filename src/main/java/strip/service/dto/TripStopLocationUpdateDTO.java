package strip.service.dto;

import java.time.Instant;

public class TripStopLocationUpdateDTO {

    private String stopLoca;
    private Instant stopLocaTime;
    private String stopLocaStatus;

    public TripStopLocationUpdateDTO(String stopLoca, Instant stopLocaTime, String stopLocaStatus) {
        this.stopLoca = stopLoca;
        this.stopLocaTime = stopLocaTime;
        this.stopLocaStatus = stopLocaStatus;
    }

    public TripStopLocationUpdateDTO() {}

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
}
