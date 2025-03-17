package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.DriverStatus;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "driver_id", length = 36)
    private UUID driverID;

    @Column(name = "usedto_driver")
    private Boolean usedtoDriver;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_status")
    private DriverStatus driverStatus;

    @Column(name = "driver_point")
    private Integer driverPoint;

    @Column(name = "banned_day")
    private Instant bannedDay;

    @Lob
    @Column(name = "driver_license")
    private byte[] driverLicense;

    @Column(name = "driver_license_content_type")
    private String driverLicenseContentType;

    @Lob
    @Column(name = "identity_card_face_up")
    private byte[] identityCardFaceUp;

    @Column(name = "identity_card_face_up_content_type")
    private String identityCardFaceUpContentType;

    @Lob
    @Column(name = "identity_card_facedown")
    private byte[] identityCardFacedown;

    @Column(name = "identity_card_facedown_content_type")
    private String identityCardFacedownContentType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "driver", "trips" }, allowSetters = true)
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "vehicle", "driver", "requestTrips", "tripStopLocations", "feedbacks", "ratings", "passengers" },
        allowSetters = true
    )
    private Set<Trip> trips = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trip", "driver", "user" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trip", "driver", "user" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "driver", "packageDriver" }, allowSetters = true)
    private Set<DriverPackageSubscription> driverPackageSubscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Driver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getDriverID() {
        return this.driverID;
    }

    public Driver driverID(UUID driverID) {
        this.setDriverID(driverID);
        return this;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public Boolean getUsedtoDriver() {
        return this.usedtoDriver;
    }

    public Driver usedtoDriver(Boolean usedtoDriver) {
        this.setUsedtoDriver(usedtoDriver);
        return this;
    }

    public void setUsedtoDriver(Boolean usedtoDriver) {
        this.usedtoDriver = usedtoDriver;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public Driver expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public DriverStatus getDriverStatus() {
        return this.driverStatus;
    }

    public Driver driverStatus(DriverStatus driverStatus) {
        this.setDriverStatus(driverStatus);
        return this;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Integer getDriverPoint() {
        return this.driverPoint;
    }

    public Driver driverPoint(Integer driverPoint) {
        this.setDriverPoint(driverPoint);
        return this;
    }

    public void setDriverPoint(Integer driverPoint) {
        this.driverPoint = driverPoint;
    }

    public Instant getBannedDay() {
        return this.bannedDay;
    }

    public Driver bannedDay(Instant bannedDay) {
        this.setBannedDay(bannedDay);
        return this;
    }

    public void setBannedDay(Instant bannedDay) {
        this.bannedDay = bannedDay;
    }

    public byte[] getDriverLicense() {
        return this.driverLicense;
    }

    public Driver driverLicense(byte[] driverLicense) {
        this.setDriverLicense(driverLicense);
        return this;
    }

    public void setDriverLicense(byte[] driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getDriverLicenseContentType() {
        return this.driverLicenseContentType;
    }

    public Driver driverLicenseContentType(String driverLicenseContentType) {
        this.driverLicenseContentType = driverLicenseContentType;
        return this;
    }

    public void setDriverLicenseContentType(String driverLicenseContentType) {
        this.driverLicenseContentType = driverLicenseContentType;
    }

    public byte[] getIdentityCardFaceUp() {
        return this.identityCardFaceUp;
    }

    public Driver identityCardFaceUp(byte[] identityCardFaceUp) {
        this.setIdentityCardFaceUp(identityCardFaceUp);
        return this;
    }

    public void setIdentityCardFaceUp(byte[] identityCardFaceUp) {
        this.identityCardFaceUp = identityCardFaceUp;
    }

    public String getIdentityCardFaceUpContentType() {
        return this.identityCardFaceUpContentType;
    }

    public Driver identityCardFaceUpContentType(String identityCardFaceUpContentType) {
        this.identityCardFaceUpContentType = identityCardFaceUpContentType;
        return this;
    }

    public void setIdentityCardFaceUpContentType(String identityCardFaceUpContentType) {
        this.identityCardFaceUpContentType = identityCardFaceUpContentType;
    }

    public byte[] getIdentityCardFacedown() {
        return this.identityCardFacedown;
    }

    public Driver identityCardFacedown(byte[] identityCardFacedown) {
        this.setIdentityCardFacedown(identityCardFacedown);
        return this;
    }

    public void setIdentityCardFacedown(byte[] identityCardFacedown) {
        this.identityCardFacedown = identityCardFacedown;
    }

    public String getIdentityCardFacedownContentType() {
        return this.identityCardFacedownContentType;
    }

    public Driver identityCardFacedownContentType(String identityCardFacedownContentType) {
        this.identityCardFacedownContentType = identityCardFacedownContentType;
        return this;
    }

    public void setIdentityCardFacedownContentType(String identityCardFacedownContentType) {
        this.identityCardFacedownContentType = identityCardFacedownContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        if (this.vehicles != null) {
            this.vehicles.forEach(i -> i.setDriver(null));
        }
        if (vehicles != null) {
            vehicles.forEach(i -> i.setDriver(this));
        }
        this.vehicles = vehicles;
    }

    public Driver vehicles(Set<Vehicle> vehicles) {
        this.setVehicles(vehicles);
        return this;
    }

    public Driver addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.setDriver(this);
        return this;
    }

    public Driver removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.setDriver(null);
        return this;
    }

    public Set<Trip> getTrips() {
        return this.trips;
    }

    public void setTrips(Set<Trip> trips) {
        if (this.trips != null) {
            this.trips.forEach(i -> i.setDriver(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setDriver(this));
        }
        this.trips = trips;
    }

    public Driver trips(Set<Trip> trips) {
        this.setTrips(trips);
        return this;
    }

    public Driver addTrip(Trip trip) {
        this.trips.add(trip);
        trip.setDriver(this);
        return this;
    }

    public Driver removeTrip(Trip trip) {
        this.trips.remove(trip);
        trip.setDriver(null);
        return this;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setDriver(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setDriver(this));
        }
        this.feedbacks = feedbacks;
    }

    public Driver feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public Driver addFeedback(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setDriver(this);
        return this;
    }

    public Driver removeFeedback(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setDriver(null);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setDriver(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setDriver(this));
        }
        this.ratings = ratings;
    }

    public Driver ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Driver addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setDriver(this);
        return this;
    }

    public Driver removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setDriver(null);
        return this;
    }

    public Set<DriverPackageSubscription> getDriverPackageSubscriptions() {
        return this.driverPackageSubscriptions;
    }

    public void setDriverPackageSubscriptions(Set<DriverPackageSubscription> driverPackageSubscriptions) {
        if (this.driverPackageSubscriptions != null) {
            this.driverPackageSubscriptions.forEach(i -> i.setDriver(null));
        }
        if (driverPackageSubscriptions != null) {
            driverPackageSubscriptions.forEach(i -> i.setDriver(this));
        }
        this.driverPackageSubscriptions = driverPackageSubscriptions;
    }

    public Driver driverPackageSubscriptions(Set<DriverPackageSubscription> driverPackageSubscriptions) {
        this.setDriverPackageSubscriptions(driverPackageSubscriptions);
        return this;
    }

    public Driver addDriverPackageSubscription(DriverPackageSubscription driverPackageSubscription) {
        this.driverPackageSubscriptions.add(driverPackageSubscription);
        driverPackageSubscription.setDriver(this);
        return this;
    }

    public Driver removeDriverPackageSubscription(DriverPackageSubscription driverPackageSubscription) {
        this.driverPackageSubscriptions.remove(driverPackageSubscription);
        driverPackageSubscription.setDriver(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Driver)) {
            return false;
        }
        return getId() != null && getId().equals(((Driver) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", driverID='" + getDriverID() + "'" +
            ", usedtoDriver='" + getUsedtoDriver() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", driverStatus='" + getDriverStatus() + "'" +
            ", driverPoint=" + getDriverPoint() +
            ", bannedDay='" + getBannedDay() + "'" +
            ", driverLicense='" + getDriverLicense() + "'" +
            ", driverLicenseContentType='" + getDriverLicenseContentType() + "'" +
            ", identityCardFaceUp='" + getIdentityCardFaceUp() + "'" +
            ", identityCardFaceUpContentType='" + getIdentityCardFaceUpContentType() + "'" +
            ", identityCardFacedown='" + getIdentityCardFacedown() + "'" +
            ", identityCardFacedownContentType='" + getIdentityCardFacedownContentType() + "'" +
            "}";
    }
}
