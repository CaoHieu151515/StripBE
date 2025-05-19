package strip.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.NotificationSourceType;
import strip.domain.enumeration.NotificationType;

@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "content")
    private String content;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "related_id", columnDefinition = "uuid")
    private UUID relatedId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private NotificationSourceType sourceType;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public NotificationSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(NotificationSourceType sourceType) {
        this.sourceType = sourceType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // --- Getters and Setters ---

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Notification date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getContent() {
        return this.content;
    }

    public Notification content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public Notification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public Notification isRead(Boolean isRead) {
        this.setIsRead(isRead);
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Notification createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public Notification relatedId(UUID relatedId) {
        this.setRelatedId(relatedId);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification user(User user) {
        this.setUser(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Notification{" +
            "id=" +
            getId() +
            ", date='" +
            getDate() +
            "'" +
            ", content='" +
            getContent() +
            "'" +
            ", title='" +
            getTitle() +
            "'" +
            ", isRead=" +
            getIsRead() +
            ", createdDate=" +
            getCreatedDate() +
            ", type=" +
            getType() +
            ", relatedId=" +
            getRelatedId() +
            "}"
        );
    }
}
