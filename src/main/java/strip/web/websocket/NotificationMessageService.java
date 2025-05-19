package strip.web.websocket;

import java.security.Principal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import strip.service.NotificationService;
import strip.service.dto.NotificationListResponseDTO;

@Controller
public class NotificationMessageService {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationMessageService(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/notification/list")
    @SendToUser("/queue/notification-list")
    public NotificationListResponseDTO getMyNotifications(Principal principal) {
        return notificationService.getMyNotificationsByLogin(principal.getName());
    }

    public void notifyUser(String login) {
        NotificationListResponseDTO notifications = notificationService.getMyNotificationsByLogin(login);
        messagingTemplate.convertAndSendToUser(login, "/queue/notification-list", notifications);
    }
}
