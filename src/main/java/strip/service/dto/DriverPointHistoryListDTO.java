package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.DriverPointHistoryStatus;

public class DriverPointHistoryListDTO {

    private UUID pointId;
    private Integer point;
    private String reason;
    private Instant date;
    private DriverPointHistoryStatus status;
    private String userName; // Người xử lý (staff/admin)

    // Getters & Setters

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
