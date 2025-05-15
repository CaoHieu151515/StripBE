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
import strip.domain.enumeration.PackageDriverStatus;

/**
 * A PackageDriver.
 */
@Entity
@Table(name = "package_driver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PackageDriver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "package_id", length = 36)
    private UUID packageID;

    @Column(name = "price")
    private Double price;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "time")
    private Integer time;

    @Column(name = "bonus")
    private Integer bonus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PackageDriverStatus status;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "expire_date")
    private Instant expireDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "packageDriver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "packageDriver", "walletTransactions" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "packageDriver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "driver", "packageDriver" }, allowSetters = true)
    private Set<DriverPackageSubscription> driverPackageSubscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PackageDriver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPackageID() {
        return this.packageID;
    }

    public PackageDriver packageID(UUID packageID) {
        this.setPackageID(packageID);
        return this;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public Double getPrice() {
        return this.price;
    }

    public PackageDriver price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public PackageDriver name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PackageDriver description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return this.time;
    }

    public PackageDriver time(Integer time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getBonus() {
        return this.bonus;
    }

    public PackageDriver bonus(Integer bonus) {
        this.setBonus(bonus);
        return this;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public PackageDriverStatus getStatus() {
        return this.status;
    }

    public PackageDriver status(PackageDriverStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PackageDriverStatus status) {
        this.status = status;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PackageDriver createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpireDate() {
        return this.expireDate;
    }

    public PackageDriver expireDate(Instant expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setPackageDriver(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setPackageDriver(this));
        }
        this.payments = payments;
    }

    public PackageDriver payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public PackageDriver addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setPackageDriver(this);
        return this;
    }

    public PackageDriver removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setPackageDriver(null);
        return this;
    }

    public Set<DriverPackageSubscription> getDriverPackageSubscriptions() {
        return this.driverPackageSubscriptions;
    }

    public void setDriverPackageSubscriptions(Set<DriverPackageSubscription> driverPackageSubscriptions) {
        if (this.driverPackageSubscriptions != null) {
            this.driverPackageSubscriptions.forEach(i -> i.setPackageDriver(null));
        }
        if (driverPackageSubscriptions != null) {
            driverPackageSubscriptions.forEach(i -> i.setPackageDriver(this));
        }
        this.driverPackageSubscriptions = driverPackageSubscriptions;
    }

    public PackageDriver driverPackageSubscriptions(Set<DriverPackageSubscription> driverPackageSubscriptions) {
        this.setDriverPackageSubscriptions(driverPackageSubscriptions);
        return this;
    }

    public PackageDriver addDriverPackageSubscription(DriverPackageSubscription driverPackageSubscription) {
        this.driverPackageSubscriptions.add(driverPackageSubscription);
        driverPackageSubscription.setPackageDriver(this);
        return this;
    }

    public PackageDriver removeDriverPackageSubscription(DriverPackageSubscription driverPackageSubscription) {
        this.driverPackageSubscriptions.remove(driverPackageSubscription);
        driverPackageSubscription.setPackageDriver(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageDriver)) {
            return false;
        }
        return getId() != null && getId().equals(((PackageDriver) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PackageDriver{" +
                "id=" + getId() +
                ", packageID='" + getPackageID() + "'" +
                ", price=" + getPrice() +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", time=" + getTime() +
                ", bonus=" + getBonus() +
                ", status='" + getStatus() + "'" +
                "}";
    }
}
