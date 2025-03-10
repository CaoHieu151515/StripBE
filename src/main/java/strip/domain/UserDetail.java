package strip.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A UserDetail.
 */
@Entity
@Table(name = "user_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "app_user_detail", length = 36)
    private UUID appUserDetail;

    @Lob
    @Column(name = "userimage")
    private byte[] userimage;

    @Column(name = "userimage_content_type")
    private String userimageContentType;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getAppUserDetail() {
        return this.appUserDetail;
    }

    public UserDetail appUserDetail(UUID appUserDetail) {
        this.setAppUserDetail(appUserDetail);
        return this;
    }

    public void setAppUserDetail(UUID appUserDetail) {
        this.appUserDetail = appUserDetail;
    }

    public byte[] getUserimage() {
        return this.userimage;
    }

    public UserDetail userimage(byte[] userimage) {
        this.setUserimage(userimage);
        return this;
    }

    public void setUserimage(byte[] userimage) {
        this.userimage = userimage;
    }

    public String getUserimageContentType() {
        return this.userimageContentType;
    }

    public UserDetail userimageContentType(String userimageContentType) {
        this.userimageContentType = userimageContentType;
        return this;
    }

    public void setUserimageContentType(String userimageContentType) {
        this.userimageContentType = userimageContentType;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserDetail phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return this.gender;
    }

    public UserDetail gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetail user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((UserDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetail{" +
            "id=" + getId() +
            ", appUserDetail='" + getAppUserDetail() + "'" +
            ", userimage='" + getUserimage() + "'" +
            ", userimageContentType='" + getUserimageContentType() + "'" +
            ", phone='" + getPhone() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
