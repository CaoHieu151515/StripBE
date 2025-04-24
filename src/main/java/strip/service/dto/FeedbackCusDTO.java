package strip.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.FeedbackStatus;
import strip.domain.enumeration.FeedbackType;

@JsonInclude(Include.NON_NULL)
public class FeedbackCusDTO {

    private UUID feedbackID;
    private FeedbackType feedbackType;
    private FeedbackStatus feedbackStatus;
    private String feedbackDescription;
    private Integer feedbackRating;
    private Instant feedbackTime;

    // Custom fields
    private TripDTO trip;
    private ConfirmingVehicleDriverDTO driver;
    private UsermanageDTO user;
    private String tripId;

    public UUID getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(UUID feedbackID) {
        this.feedbackID = feedbackID;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public Integer getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(Integer feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public UsermanageDTO getUser() {
        return user;
    }

    public void setUser(UsermanageDTO user) {
        this.user = user;
    }

    public ConfirmingVehicleDriverDTO getDriver() {
        return driver;
    }

    public void setDriver(ConfirmingVehicleDriverDTO driver) {
        this.driver = driver;
    }

    public Instant getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Instant feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}
