package strip.web.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import strip.domain.User;
import strip.service.NotificationService;
import strip.service.UserService;
import strip.service.dto.NotificationCreateDTO;
import strip.service.dto.NotificationNewDTO;
import strip.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationNewResource {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationNewResource(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * GET /api/notifications/my : Get notifications for current user.
     */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationNewDTO>> getMyNotifications() {
        User user = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "notification", "user-not-found"));
        List<NotificationNewDTO> notifications = notificationService.getMyNotifications(user.getId());
        return ResponseEntity.ok(notifications);
    }

    /**
     * POST /api/notifications : Create a new notification
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> createNotification(@RequestBody NotificationCreateDTO dto) {
        if (dto.getTitle() == null || dto.getContent() == null || dto.getUserId() == null) {
            throw new BadRequestAlertException("Missing fields", "notification", "missing-fields");
        }

        notificationService.createNotification(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * PUT /api/notifications/{id}/read : Mark notification as read.
     */
    @PatchMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
