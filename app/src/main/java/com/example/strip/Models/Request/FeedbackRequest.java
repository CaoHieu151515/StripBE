package com.example.strip.Models.Request;

public class FeedbackRequest {
    private String feedbackDescription;
    private int feedbackRating;

    public FeedbackRequest(String feedbackDescription, int feedbackRating) {
        this.feedbackDescription = feedbackDescription;
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public int getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(int feedbackRating) {
        this.feedbackRating = feedbackRating;
    }
}
