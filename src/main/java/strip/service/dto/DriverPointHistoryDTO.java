package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.DriverPointHistoryStatus;

public class DriverPointHistoryDTO {

    private UUID pointId;
    private Integer point;
    private String reason;
    private Instant date;
    private DriverPointHistoryStatus status;

    private UUID driverId;
    private UUID userId;

    public UUID getPointId() {
        return pointId;
    }

    public void setPointId(UUID pointId) {
        this.pointId = pointId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public DriverPointHistoryStatus getStatus() {
        return status;
    }

    public void setStatus(DriverPointHistoryStatus status) {
        this.status = status;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
