package strip.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Notification;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.enumeration.NotificationSourceType;
import strip.repository.NotificationRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.service.dto.NotificationCreateDTO;
import strip.service.dto.NotificationDTO;
import strip.service.dto.NotificationListResponseDTO;
import strip.service.dto.NotificationNewDTO;
import strip.service.mapper.NotificationMapper;
import strip.web.rest.errors.BadRequestAlertException;
import strip.web.websocket.NotificationMessageService;

/**
 * Service Implementation for managing {@link strip.domain.Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final UserRepository userRepository;

    private final UserDetailRepository userDetailRepository;

    private final NotificationMessageService notificationMessageService;

    public NotificationService(
        NotificationRepository notificationRepository,
        NotificationMapper notificationMapper,
        UserRepository userRepository,
        UserDetailRepository userDetailRepository,
        @Lazy NotificationMessageService notificationMessageService
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.notificationMessageService = notificationMessageService;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        LOG.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Update a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO update(NotificationDTO notificationDTO) {
        LOG.debug("Request to update Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        LOG.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    public void createNotification(NotificationCreateDTO dto) {
        if (dto.getUserId() == null) {
            throw new BadRequestAlertException("User ID is required", "notification", "user-id-null");
        }

        UserDetail detail = userDetailRepository
            .findByAppUserDetail(dto.getUserId())
            .orElseThrow(() -> new BadRequestAlertException("UserDetail not found", "notification", "user-detail-not-found"));

        User user = userRepository
            .findById(detail.getUser().getId())
            .orElseThrow(() -> new BadRequestAlertException("User not found", "notification", "user-not-found"));

        Notification notify = new Notification();
        notify.setTitle(dto.getTitle());
        notify.setContent(dto.getContent());
        notify.setDate(Instant.now());
        notify.setCreatedDate(Instant.now());
        notify.setIsRead(false);
        notify.setUser(user);

        // ✅ Gán type và relatedId nếu có
        notify.setType(dto.getType());
        notify.setRelatedId(dto.getRelatedId());
        notify.setSourceType(NotificationSourceType.USER);

        notificationRepository.save(notify);
        notificationMessageService.notifyUser(user.getLogin());
    }

    public void createSystemNotification(NotificationCreateDTO dto) {
        if (dto.getUserId() == null) {
            throw new BadRequestAlertException("User ID is required", "notification", "user-id-null");
        }

        UserDetail detail = userDetailRepository
            .findByAppUserDetail(dto.getUserId())
            .orElseThrow(() -> new BadRequestAlertException("UserDetail not found", "notification", "user-detail-not-found"));

        User user = userRepository
            .findById(detail.getUser().getId())
            .orElseThrow(() -> new BadRequestAlertException("User not found", "notification", "user-not-found"));

        Notification notify = new Notification();
        notify.setTitle(dto.getTitle());
        notify.setContent(dto.getContent());
        notify.setDate(Instant.now());
        notify.setCreatedDate(Instant.now());
        notify.setIsRead(false);
        notify.setUser(user);

        notify.setType(dto.getType());
        notify.setRelatedId(dto.getRelatedId());

        // ✅ Gán cứng hệ thống
        notify.setSourceType(NotificationSourceType.SYSTEM);

        notificationRepository.save(notify);
        notificationMessageService.notifyUser(user.getLogin());
    }

    public List<NotificationNewDTO> getMyNotifications(Long userId) {
        return notificationRepository.findAllByUser_IdOrderByCreatedDateDesc(userId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public void markAsRead(Long id) {
        Notification notify = notificationRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Notification not found", "notification", "not-found"));
        notify.setIsRead(true);
        notificationRepository.save(notify);
    }

    private NotificationNewDTO toDto(Notification entity) {
        NotificationNewDTO dto = new NotificationNewDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setIsRead(entity.getIsRead());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return dto;
    }

    public NotificationListResponseDTO getMyNotificationsByLogin(String login) {
        User user = userRepository
            .findOneByLogin(login)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "notification", "user-not-found"));

        List<NotificationNewDTO> list = notificationRepository
            .findAllByUser_IdOrderByCreatedDateDesc(user.getId())
            .stream()
            .map(this::toDto)
            .toList();

        long unreadCount = notificationRepository.countByUser_IdAndIsReadFalse(user.getId());

        return new NotificationListResponseDTO(list, unreadCount);
    }

    @Transactional(readOnly = true)
    public List<NotificationNewDTO> getAllSystemNotifications() {
        return notificationRepository
            .findAllBySourceTypeOrderByCreatedDateDesc(NotificationSourceType.SYSTEM)
            .stream()
            .map(this::toDto)
            .toList();
    }
}
