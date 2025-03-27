package strip.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.ApplicationType;

/**
 * A SendingAplication.
 */
@Entity
@Table(name = "sending_aplication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SendingAplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "apli_id", length = 36)
    private UUID apliID;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_application_type")
    private ApplicationType sendApplicationType;

    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "img")
    private byte[] img;

    @Column(name = "img_content_type")
    private String imgContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SendingAplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getApliID() {
        return this.apliID;
    }

    public SendingAplication apliID(UUID apliID) {
        this.setApliID(apliID);
        return this;
    }

    public void setApliID(UUID apliID) {
        this.apliID = apliID;
    }

    public ApplicationType getSendApplicationType() {
        return this.sendApplicationType;
    }

    public SendingAplication sendApplicationType(ApplicationType sendApplicationType) {
        this.setSendApplicationType(sendApplicationType);
        return this;
    }

    public void setSendApplicationType(ApplicationType sendApplicationType) {
        this.sendApplicationType = sendApplicationType;
    }

    public String getContent() {
        return this.content;
    }

    public SendingAplication content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImg() {
        return this.img;
    }

    public SendingAplication img(byte[] img) {
        this.setImg(img);
        return this;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getImgContentType() {
        return this.imgContentType;
    }

    public SendingAplication imgContentType(String imgContentType) {
        this.imgContentType = imgContentType;
        return this;
    }

    public void setImgContentType(String imgContentType) {
        this.imgContentType = imgContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SendingAplication user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendingAplication)) {
            return false;
        }
        return getId() != null && getId().equals(((SendingAplication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SendingAplication{" +
            "id=" + getId() +
            ", apliID='" + getApliID() + "'" +
            ", sendApplicationType='" + getSendApplicationType() + "'" +
            ", content='" + getContent() + "'" +
            ", img='" + getImg() + "'" +
            ", imgContentType='" + getImgContentType() + "'" +
            "}";
    }
}
