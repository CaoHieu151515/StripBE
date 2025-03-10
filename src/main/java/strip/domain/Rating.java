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
import strip.domain.enumeration.RatingType;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "rating_id", length = 36)
    private UUID ratingID;

    @Column(name = "rating_time")
    private Instant ratingTime;

    @Column(name = "rating_driver")
    private Integer ratingDriver;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating_type")
    private RatingType ratingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "driver", "passengers", "tripStopLocations", "feedbacks", "ratings" }, allowSetters = true)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "vehicles", "trips", "feedbacks", "ratings" }, allowSetters = true)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rating id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRatingID() {
        return this.ratingID;
    }

    public Rating ratingID(UUID ratingID) {
        this.setRatingID(ratingID);
        return this;
    }

    public void setRatingID(UUID ratingID) {
        this.ratingID = ratingID;
    }

    public Instant getRatingTime() {
        return this.ratingTime;
    }

    public Rating ratingTime(Instant ratingTime) {
        this.setRatingTime(ratingTime);
        return this;
    }

    public void setRatingTime(Instant ratingTime) {
        this.ratingTime = ratingTime;
    }

    public Integer getRatingDriver() {
        return this.ratingDriver;
    }

    public Rating ratingDriver(Integer ratingDriver) {
        this.setRatingDriver(ratingDriver);
        return this;
    }

    public void setRatingDriver(Integer ratingDriver) {
        this.ratingDriver = ratingDriver;
    }

    public RatingType getRatingType() {
        return this.ratingType;
    }

    public Rating ratingType(RatingType ratingType) {
        this.setRatingType(ratingType);
        return this;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Rating trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Rating driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rating user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return getId() != null && getId().equals(((Rating) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", ratingID='" + getRatingID() + "'" +
            ", ratingTime='" + getRatingTime() + "'" +
            ", ratingDriver=" + getRatingDriver() +
            ", ratingType='" + getRatingType() + "'" +
            "}";
    }
}
