package strip.service.dto;

import java.io.Serializable;
import java.util.UUID;
import strip.domain.enumeration.NotificationType;

public class NotificationCreateDTO implements Serializable {

    private String title;
    private String content;
    private UUID userId;

    private NotificationType type;
    private UUID relatedId;

    public NotificationCreateDTO() {}

    public NotificationCreateDTO(String title, String content, UUID userId, NotificationType type, UUID relatedId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.type = type;
        this.relatedId = relatedId;
    }

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
