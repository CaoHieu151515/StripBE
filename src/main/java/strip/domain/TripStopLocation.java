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

/**
 * A TripStopLocation.
 */
@Entity
@Table(name = "trip_stop_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripStopLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "stop_loca_id", length = 36)
    private UUID stopLocaID;

    @Column(name = "stop_loca")
    private String stopLoca;

    @Column(name = "stoploca_position")
    private Integer stoplocaPosition;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "estimated_km")
    private Double estimatedKM;

    @Column(name = "stop_loca_time")
    private Instant stopLocaTime;

    @Column(name = "stop_loca_status")
    private String stopLocaStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "vehicle", "driver", "requestTrips", "tripStopLocations", "feedbacks", "reports", "ratings" },
        allowSetters = true
    )
    private Trip trip;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripStopLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getStopLocaID() {
        return this.stopLocaID;
    }

    public TripStopLocation stopLocaID(UUID stopLocaID) {
        this.setStopLocaID(stopLocaID);
        return this;
    }

    public void setStopLocaID(UUID stopLocaID) {
        this.stopLocaID = stopLocaID;
    }

    public String getStopLoca() {
        return this.stopLoca;
    }

    public TripStopLocation stopLoca(String stopLoca) {
        this.setStopLoca(stopLoca);
        return this;
    }

    public void setStopLoca(String stopLoca) {
        this.stopLoca = stopLoca;
    }

    public Integer getStoplocaPosition() {
        return this.stoplocaPosition;
    }

    public TripStopLocation stoplocaPosition(Integer stoplocaPosition) {
        this.setStoplocaPosition(stoplocaPosition);
        return this;
    }

    public void setStoplocaPosition(Integer stoplocaPosition) {
        this.stoplocaPosition = stoplocaPosition;
    }

    public Integer getEstimatedTime() {
        return this.estimatedTime;
    }

    public TripStopLocation estimatedTime(Integer estimatedTime) {
        this.setEstimatedTime(estimatedTime);
        return this;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Double getEstimatedKM() {
        return this.estimatedKM;
    }

    public TripStopLocation estimatedKM(Double estimatedKM) {
        this.setEstimatedKM(estimatedKM);
        return this;
    }

    public void setEstimatedKM(Double estimatedKM) {
        this.estimatedKM = estimatedKM;
    }

    public Instant getStopLocaTime() {
        return this.stopLocaTime;
    }

    public TripStopLocation stopLocaTime(Instant stopLocaTime) {
        this.setStopLocaTime(stopLocaTime);
        return this;
    }

    public void setStopLocaTime(Instant stopLocaTime) {
        this.stopLocaTime = stopLocaTime;
    }

    public String getStopLocaStatus() {
        return this.stopLocaStatus;
    }

    public TripStopLocation stopLocaStatus(String stopLocaStatus) {
        this.setStopLocaStatus(stopLocaStatus);
        return this;
    }

    public void setStopLocaStatus(String stopLocaStatus) {
        this.stopLocaStatus = stopLocaStatus;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public TripStopLocation trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripStopLocation)) {
            return false;
        }
        return getId() != null && getId().equals(((TripStopLocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripStopLocation{" +
            "id=" + getId() +
            ", stopLocaID='" + getStopLocaID() + "'" +
            ", stopLoca='" + getStopLoca() + "'" +
            ", stoplocaPosition=" + getStoplocaPosition() +
            ", estimatedTime=" + getEstimatedTime() +
            ", estimatedKM=" + getEstimatedKM() +
            ", stopLocaTime='" + getStopLocaTime() + "'" +
            ", stopLocaStatus='" + getStopLocaStatus() + "'" +
            "}";
    }
}
