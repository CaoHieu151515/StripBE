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
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;

/**
 * A UserWallet.
 */
@Entity
@Table(name = "user_wallet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_wallet", length = 36)
    private UUID userWallet;

    @Column(name = "jhi_before")
    private Double before;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "current")
    private Double current;

    @Column(name = "mobify_date")
    private Instant mobifyDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "systemWallet", "payment", "userWallet", "systemTempWallet" }, allowSetters = true)
    private Set<WalletTransaction> walletTransactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserWallet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserWallet() {
        return this.userWallet;
    }

    public UserWallet userWallet(UUID userWallet) {
        this.setUserWallet(userWallet);
        return this;
    }

    public void setUserWallet(UUID userWallet) {
        this.userWallet = userWallet;
    }

    public Double getBefore() {
        return this.before;
    }

    public UserWallet before(Double before) {
        this.setBefore(before);
        return this;
    }

    public void setBefore(Double before) {
        this.before = before;
    }

    public Double getAmount() {
        return this.amount;
    }

    public UserWallet amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCurrent() {
        return this.current;
    }

    public UserWallet current(Double current) {
        this.setCurrent(current);
        return this;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Instant getMobifyDate() {
        return this.mobifyDate;
    }

    public UserWallet mobifyDate(Instant mobifyDate) {
        this.setMobifyDate(mobifyDate);
        return this;
    }

    public void setMobifyDate(Instant mobifyDate) {
        this.mobifyDate = mobifyDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserWallet user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<WalletTransaction> getWalletTransactions() {
        return this.walletTransactions;
    }

    public void setWalletTransactions(Set<WalletTransaction> walletTransactions) {
        if (this.walletTransactions != null) {
            this.walletTransactions.forEach(i -> i.setUserWallet(null));
        }
        if (walletTransactions != null) {
            walletTransactions.forEach(i -> i.setUserWallet(this));
        }
        this.walletTransactions = walletTransactions;
    }

    public UserWallet walletTransactions(Set<WalletTransaction> walletTransactions) {
        this.setWalletTransactions(walletTransactions);
        return this;
    }

    public UserWallet addWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransactions.add(walletTransaction);
        walletTransaction.setUserWallet(this);
        return this;
    }

    public UserWallet removeWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransactions.remove(walletTransaction);
        walletTransaction.setUserWallet(null);
        return this;
    }

    public UserWallet addWalletTransactionAndUpdateBalance(WalletTransaction transaction) {
        if (transaction == null) return this;

        transaction.setUserWallet(this);
        this.walletTransactions.add(transaction);

        if (transaction.getTransStatus() == TransactionStatus.SUCCESS) {
            double amount = transaction.getAmount() != null ? transaction.getAmount() : 0.0;
            WalletTransactionType type = transaction.getWalletType();

            if (this.current == null) {
                this.current = 0.0;
            }

            // Những loại giao dịch trừ tiền từ ví người dùng
            switch (type) {
                case WITHDRAW:
                    this.current -= amount;
                    break;
                case DRIVER_CREATE_TRIP_FEE:
                    this.current -= amount;
                    break;
                case DRIVER_DONE_TRIP_FEE:
                    this.current -= amount;
                    break;
                case PASSENGER_APPROVE_FEE:
                    this.current -= amount;
                    break;
                case DRIVER_BUY_PACKAGE:
                    this.current -= amount;
                    break;
                // Những loại giao dịch cộng tiền vào ví người dùng
                case DEPOSIT:
                    this.current += amount;
                    break;
                case REFUND:
                    this.current += amount;
                    break;
                case DRIVER_DONE_TRIP_REFUND:
                    this.current += amount;
                    break;
                // Những loại hệ thống thu, không tác động ví user
                case SYSTEM_GAIN_CREATE_TRIP_FEE:
                    this.current += amount;
                    break;
                case SYSTEM_GAIN_PASSENGER_APPROVE_FEE:
                    this.current += amount;
                    break;
                case SYSTEM_GAIN_DONE_TRIP_FEE:
                    this.current += amount;
                    break;
                case SYSTEM_GAIN_PACKAGE_FEE:
                    this.current += amount;
                    break;
                default:
                    break;
            }
        }

        this.mobifyDate = Instant.now();
        return this;
    }

    public void recalculateBalance() {
        double newCurrent = 0.0;

        for (WalletTransaction tx : walletTransactions) {
            if (tx.getTransStatus() != TransactionStatus.SUCCESS || tx.getWalletType() == null) continue;

            double amount = tx.getAmount() != null ? tx.getAmount() : 0.0;

            switch (tx.getWalletType()) {
                case DEPOSIT:
                case REFUND:
                case DRIVER_DONE_TRIP_REFUND:
                    newCurrent += amount;
                    break;
                case WITHDRAW:
                case DRIVER_CREATE_TRIP_FEE:
                case DRIVER_DONE_TRIP_FEE:
                case PASSENGER_APPROVE_FEE:
                    newCurrent -= amount;
                    break;
                default:
                    break;
            }
        }

        this.current = newCurrent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserWallet)) {
            return false;
        }
        return getId() != null && getId().equals(((UserWallet) o).getId());
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
        return "UserWallet{" +
                "id=" + getId() +
                ", userWallet='" + getUserWallet() + "'" +
                ", before=" + getBefore() +
                ", amount=" + getAmount() +
                ", current=" + getCurrent() +
                ", mobifyDate='" + getMobifyDate() + "'" +
                "}";
    }
}
