package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.TripStatus;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "trip_id", length = 36)
    private UUID tripID;

    @Lob
    @Column(name = "trip_img")
    private byte[] tripImg;

    @Column(name = "trip_img_content_type")
    private String tripImgContentType;

    @Column(name = "price_per_seat")
    private Double pricePerSeat;

    @Column(name = "max_seat")
    private Integer maxSeat;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "end_location")
    private String endLocation;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_condition")
    private String condition;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status")
    private TripStatus tripStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "vehicles", "trips", "feedbacks", "ratings" }, allowSetters = true)
    private Driver driver;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trip", "user" }, allowSetters = true)
    private Set<Passenger> passengers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trip" }, allowSetters = true)
    private Set<TripStopLocation> tripStopLocations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trip", "driver", "user" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trip", "driver", "user" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTripID() {
        return this.tripID;
    }

    public Trip tripID(UUID tripID) {
        this.setTripID(tripID);
        return this;
    }

    public void setTripID(UUID tripID) {
        this.tripID = tripID;
    }

    public byte[] getTripImg() {
        return this.tripImg;
    }

    public Trip tripImg(byte[] tripImg) {
        this.setTripImg(tripImg);
        return this;
    }

    public void setTripImg(byte[] tripImg) {
        this.tripImg = tripImg;
    }

    public String getTripImgContentType() {
        return this.tripImgContentType;
    }

    public Trip tripImgContentType(String tripImgContentType) {
        this.tripImgContentType = tripImgContentType;
        return this;
    }

    public void setTripImgContentType(String tripImgContentType) {
        this.tripImgContentType = tripImgContentType;
    }

    public Double getPricePerSeat() {
        return this.pricePerSeat;
    }

    public Trip pricePerSeat(Double pricePerSeat) {
        this.setPricePerSeat(pricePerSeat);
        return this;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public Integer getMaxSeat() {
        return this.maxSeat;
    }

    public Trip maxSeat(Integer maxSeat) {
        this.setMaxSeat(maxSeat);
        return this;
    }

    public void setMaxSeat(Integer maxSeat) {
        this.maxSeat = maxSeat;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Trip startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Trip endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public Trip startLocation(String startLocation) {
        this.setStartLocation(startLocation);
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return this.endLocation;
    }

    public Trip endLocation(String endLocation) {
        this.setEndLocation(endLocation);
        return this;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getDescription() {
        return this.description;
    }

    public Trip description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return this.condition;
    }

    public Trip condition(String condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCancelReason() {
        return this.cancelReason;
    }

    public Trip cancelReason(String cancelReason) {
        this.setCancelReason(cancelReason);
        return this;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public TripStatus getTripStatus() {
        return this.tripStatus;
    }

    public Trip tripStatus(TripStatus tripStatus) {
        this.setTripStatus(tripStatus);
        return this;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Trip driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public Set<Passenger> getPassengers() {
        return this.passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        if (this.passengers != null) {
            this.passengers.forEach(i -> i.setTrip(null));
        }
        if (passengers != null) {
            passengers.forEach(i -> i.setTrip(this));
        }
        this.passengers = passengers;
    }

    public Trip passengers(Set<Passenger> passengers) {
        this.setPassengers(passengers);
        return this;
    }

    public Trip addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.setTrip(this);
        return this;
    }

    public Trip removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setTrip(null);
        return this;
    }

    public Set<TripStopLocation> getTripStopLocations() {
        return this.tripStopLocations;
    }

    public void setTripStopLocations(Set<TripStopLocation> tripStopLocations) {
        if (this.tripStopLocations != null) {
            this.tripStopLocations.forEach(i -> i.setTrip(null));
        }
        if (tripStopLocations != null) {
            tripStopLocations.forEach(i -> i.setTrip(this));
        }
        this.tripStopLocations = tripStopLocations;
    }

    public Trip tripStopLocations(Set<TripStopLocation> tripStopLocations) {
        this.setTripStopLocations(tripStopLocations);
        return this;
    }

    public Trip addTripStopLocation(TripStopLocation tripStopLocation) {
        this.tripStopLocations.add(tripStopLocation);
        tripStopLocation.setTrip(this);
        return this;
    }

    public Trip removeTripStopLocation(TripStopLocation tripStopLocation) {
        this.tripStopLocations.remove(tripStopLocation);
        tripStopLocation.setTrip(null);
        return this;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setTrip(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setTrip(this));
        }
        this.feedbacks = feedbacks;
    }

    public Trip feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public Trip addFeedback(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setTrip(this);
        return this;
    }

    public Trip removeFeedback(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setTrip(null);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setTrip(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setTrip(this));
        }
        this.ratings = ratings;
    }

    public Trip ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Trip addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setTrip(this);
        return this;
    }

    public Trip removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setTrip(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return getId() != null && getId().equals(((Trip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", tripID='" + getTripID() + "'" +
            ", tripImg='" + getTripImg() + "'" +
            ", tripImgContentType='" + getTripImgContentType() + "'" +
            ", pricePerSeat=" + getPricePerSeat() +
            ", maxSeat=" + getMaxSeat() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startLocation='" + getStartLocation() + "'" +
            ", endLocation='" + getEndLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", condition='" + getCondition() + "'" +
            ", cancelReason='" + getCancelReason() + "'" +
            ", tripStatus='" + getTripStatus() + "'" +
            "}";
    }
}
