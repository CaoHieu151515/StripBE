package strip.service.dto;

import java.util.UUID;
import strip.domain.enumeration.FeedbackStatus;
import strip.domain.enumeration.FeedbackType;

public class FeedbackCusDTO {

    private UUID feedbackID;
    private FeedbackType feedbackType;
    private FeedbackStatus feedbackStatus;
    private String feedbackDescription;
    private Integer feedbackRating;

    // Custom fields
    private UUID tripId;
    private UUID driverId;
    private UUID userId;

    public UUID getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(UUID feedbackID) {
        this.feedbackID = feedbackID;
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

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
