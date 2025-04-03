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
import strip.domain.enumeration.DriverPointHistoryStatus;

/**
 * A DriverPointHistory.
 */
@Entity
@Table(name = "driver_point_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DriverPointHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "point_id", length = 36)
    private UUID pointId;

    @Column(name = "point")
    private Integer point;

    @Column(name = "reason")
    private String reason;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DriverPointHistoryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "driverPointHistories", "vehicles", "trips", "feedbacks", "reports", "ratings", "driverPackageSubscriptions" },
        allowSetters = true
    )
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "driverPointHistories" }, allowSetters = true)
    private UserDetail userDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DriverPointHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPointId() {
        return this.pointId;
    }

    public DriverPointHistory pointId(UUID pointId) {
        this.setPointId(pointId);
        return this;
    }

    public void setPointId(UUID pointId) {
        this.pointId = pointId;
    }

    public Integer getPoint() {
        return this.point;
    }

    public DriverPointHistory point(Integer point) {
        this.setPoint(point);
        return this;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getReason() {
        return this.reason;
    }

    public DriverPointHistory reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getDate() {
        return this.date;
    }

    public DriverPointHistory date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public DriverPointHistoryStatus getStatus() {
        return this.status;
    }

    public DriverPointHistory status(DriverPointHistoryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DriverPointHistoryStatus status) {
        this.status = status;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public DriverPointHistory driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public UserDetail getUserDetail() {
        return this.userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public DriverPointHistory userDetail(UserDetail userDetail) {
        this.setUserDetail(userDetail);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DriverPointHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((DriverPointHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverPointHistory{" +
            "id=" + getId() +
            ", pointId='" + getPointId() + "'" +
            ", point=" + getPoint() +
            ", reason='" + getReason() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
