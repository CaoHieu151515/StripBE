package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.ReportStatus;
import strip.domain.enumeration.ReportType;

public class ReportCusDTO {

    private UUID reportID;
    private ReportType reportType;
    private ReportStatus reportStatus;
    private String content;
    private Instant date;

    private UUID tripId;
    private UUID userId;
    private UUID driverId;

    public UUID getReportID() {
        return reportID;
    }

    public void setReportID(UUID reportID) {
        this.reportID = reportID;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }
}
