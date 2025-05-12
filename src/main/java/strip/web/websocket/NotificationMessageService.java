package strip.web.websocket;

import java.security.Principal;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import strip.service.NotificationService;
import strip.service.dto.NotificationNewDTO;

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
    public List<NotificationNewDTO> getMyNotifications(Principal principal) {
        System.out.println("👤 Principal: " + principal);
        System.out.println("👤 Username: " + principal.getName());
        return notificationService.getMyNotificationsByLogin(principal.getName());
    }

    // ✅ Method push realtime cho FE khi có thông báo mới
    public void notifyUser(String login) {
        List<NotificationNewDTO> notifications = notificationService.getMyNotificationsByLogin(login);
        messagingTemplate.convertAndSendToUser(
            login, // username của người nhận
            "/queue/notification-list", // FE đã subscribe
            notifications
        );
    }
}
