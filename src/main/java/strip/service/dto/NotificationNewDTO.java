package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.NotificationSourceType;
import strip.domain.enumeration.NotificationType;

public class NotificationNewDTO {

    private Long id;
    private String title;
    private String content;
    private Boolean isRead;
    private Instant createdDate;
    private NotificationType type;
    private UUID relatedId;
    private NotificationSourceType sourceType;
    private Long userId;

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public NotificationSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(NotificationSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
