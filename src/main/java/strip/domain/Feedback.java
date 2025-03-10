package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.FeedbackStatus;
import strip.domain.enumeration.FeedbackType;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "feedback_id", length = 36)
    private UUID feedbackID;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type")
    private FeedbackType feedbackType;

    @Column(name = "feedback_description")
    private String feedbackDescription;

    @Column(name = "feedback_rating")
    private Integer feedbackRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_status")
    private FeedbackStatus feedbackStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "vehicle", "driver", "passengers", "tripStopLocations", "feedbacks", "ratings" }, allowSetters = true)
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

    public Feedback id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getFeedbackID() {
        return this.feedbackID;
    }

    public Feedback feedbackID(UUID feedbackID) {
        this.setFeedbackID(feedbackID);
        return this;
    }

    public void setFeedbackID(UUID feedbackID) {
        this.feedbackID = feedbackID;
    }

    public FeedbackType getFeedbackType() {
        return this.feedbackType;
    }

    public Feedback feedbackType(FeedbackType feedbackType) {
        this.setFeedbackType(feedbackType);
        return this;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackDescription() {
        return this.feedbackDescription;
    }

    public Feedback feedbackDescription(String feedbackDescription) {
        this.setFeedbackDescription(feedbackDescription);
        return this;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public Integer getFeedbackRating() {
        return this.feedbackRating;
    }

    public Feedback feedbackRating(Integer feedbackRating) {
        this.setFeedbackRating(feedbackRating);
        return this;
    }

    public void setFeedbackRating(Integer feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public FeedbackStatus getFeedbackStatus() {
        return this.feedbackStatus;
    }

    public Feedback feedbackStatus(FeedbackStatus feedbackStatus) {
        this.setFeedbackStatus(feedbackStatus);
        return this;
    }

    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Feedback trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Feedback driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Feedback user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedback)) {
            return false;
        }
        return getId() != null && getId().equals(((Feedback) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", feedbackID='" + getFeedbackID() + "'" +
            ", feedbackType='" + getFeedbackType() + "'" +
            ", feedbackDescription='" + getFeedbackDescription() + "'" +
            ", feedbackRating=" + getFeedbackRating() +
            ", feedbackStatus='" + getFeedbackStatus() + "'" +
            "}";
    }
}
