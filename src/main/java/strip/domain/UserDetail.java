package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "address")
    private String address;

    @Column(name = "dob")
    private Instant dob;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDetail")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "driver", "userDetail" }, allowSetters = true)
    private Set<DriverPointHistory> driverPointHistories = new HashSet<>();

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

    public String getAddress() {
        return this.address;
    }

    public UserDetail address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getDob() {
        return this.dob;
    }

    public UserDetail dob(Instant dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
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

    public Set<DriverPointHistory> getDriverPointHistories() {
        return this.driverPointHistories;
    }

    public void setDriverPointHistories(Set<DriverPointHistory> driverPointHistories) {
        if (this.driverPointHistories != null) {
            this.driverPointHistories.forEach(i -> i.setUserDetail(null));
        }
        if (driverPointHistories != null) {
            driverPointHistories.forEach(i -> i.setUserDetail(this));
        }
        this.driverPointHistories = driverPointHistories;
    }

    public UserDetail driverPointHistories(Set<DriverPointHistory> driverPointHistories) {
        this.setDriverPointHistories(driverPointHistories);
        return this;
    }

    public UserDetail addDriverPointHistory(DriverPointHistory driverPointHistory) {
        this.driverPointHistories.add(driverPointHistory);
        driverPointHistory.setUserDetail(this);
        return this;
    }

    public UserDetail removeDriverPointHistory(DriverPointHistory driverPointHistory) {
        this.driverPointHistories.remove(driverPointHistory);
        driverPointHistory.setUserDetail(null);
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
            ", address='" + getAddress() + "'" +
            ", dob='" + getDob() + "'" +
            "}";
    }
}
