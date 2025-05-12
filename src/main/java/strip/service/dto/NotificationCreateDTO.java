package strip.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class NotificationCreateDTO implements Serializable {

    private String title;
    private String content;
    private UUID userId;

    public NotificationCreateDTO() {}

    public NotificationCreateDTO(String title, String content, UUID userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
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
