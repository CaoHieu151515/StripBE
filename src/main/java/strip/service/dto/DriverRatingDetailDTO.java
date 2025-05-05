package strip.service.dto;

import java.time.Instant;

public class DriverRatingDetailDTO {

    private String avatarUser; // URL avatar user/passenger
    private Instant ratingDate; // ngày rating
    private String userName; // tên user (login hoặc fullname)
    private String feedbackContent; // nội dung feedback
    private Integer ratingValue; // số sao

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public Instant getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Instant ratingDate) {
        this.ratingDate = ratingDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
}
