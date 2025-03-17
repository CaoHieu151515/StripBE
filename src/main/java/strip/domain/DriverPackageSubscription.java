package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A DriverPackageSubscription.
 */
@Entity
@Table(name = "driver_package_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DriverPackageSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "purchase_date")
    private Instant purchaseDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "package_price")
    private Double packagePrice;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "vehicles", "trips", "feedbacks", "ratings", "driverPackageSubscriptions" },
        allowSetters = true
    )
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "payments", "driverPackageSubscriptions" }, allowSetters = true)
    private PackageDriver packageDriver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public DriverPackageSubscription id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getPurchaseDate() {
        return this.purchaseDate;
    }

    public DriverPackageSubscription purchaseDate(Instant purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(Instant purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public DriverPackageSubscription expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getPackagePrice() {
        return this.packagePrice;
    }

    public DriverPackageSubscription packagePrice(Double packagePrice) {
        this.setPackagePrice(packagePrice);
        return this;
    }

    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public Boolean getActive() {
        return this.active;
    }

    public DriverPackageSubscription active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public DriverPackageSubscription driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public PackageDriver getPackageDriver() {
        return this.packageDriver;
    }

    public void setPackageDriver(PackageDriver packageDriver) {
        this.packageDriver = packageDriver;
    }

    public DriverPackageSubscription packageDriver(PackageDriver packageDriver) {
        this.setPackageDriver(packageDriver);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DriverPackageSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((DriverPackageSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverPackageSubscription{" +
            "id=" + getId() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", packagePrice=" + getPackagePrice() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
