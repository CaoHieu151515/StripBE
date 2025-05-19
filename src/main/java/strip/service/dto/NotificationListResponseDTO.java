package strip.service.dto;

import java.util.List;

public class NotificationListResponseDTO {

    private List<NotificationNewDTO> notifications;
    private long unreadCount;

    public NotificationListResponseDTO(List<NotificationNewDTO> notifications, long unreadCount) {
        this.notifications = notifications;
        this.unreadCount = unreadCount;
    }

    public List<NotificationNewDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationNewDTO> notifications) {
        this.notifications = notifications;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
    // Getters & Setters

}
