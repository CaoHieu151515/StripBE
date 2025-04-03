package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.ReportStatus;
import strip.domain.enumeration.ReportType;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "report_id", length = 36)
    private UUID reportID;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    @Column(name = "date")
    private Instant date;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatus reportStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "vehicle", "driver", "requestTrips", "tripStopLocations", "feedbacks", "reports", "ratings" },
        allowSetters = true
    )
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "driverPointHistories", "vehicles", "trips", "feedbacks", "reports", "ratings", "driverPackageSubscriptions" },
        allowSetters = true
    )
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getReportID() {
        return this.reportID;
    }

    public Report reportID(UUID reportID) {
        this.setReportID(reportID);
        return this;
    }

    public void setReportID(UUID reportID) {
        this.reportID = reportID;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public Report reportType(ReportType reportType) {
        this.setReportType(reportType);
        return this;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Instant getDate() {
        return this.date;
    }

    public Report date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getContent() {
        return this.content;
    }

    public Report content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReportStatus getReportStatus() {
        return this.reportStatus;
    }

    public Report reportStatus(ReportStatus reportStatus) {
        this.setReportStatus(reportStatus);
        return this;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Report trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Report driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Report user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return getId() != null && getId().equals(((Report) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", reportID='" + getReportID() + "'" +
            ", reportType='" + getReportType() + "'" +
            ", date='" + getDate() + "'" +
            ", content='" + getContent() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            "}";
    }
}
