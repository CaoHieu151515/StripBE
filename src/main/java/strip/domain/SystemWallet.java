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
 * A SystemWallet.
 */
@Entity
@Table(name = "system_wallet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "system_wallet_id", length = 36)
    private UUID systemWalletID;

    @Column(name = "jhi_before")
    private Double before;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "current")
    private Double current;

    @Column(name = "block_amount")
    private Double blockAmount;

    @Column(name = "mobify_date")
    private Instant mobifyDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "systemWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "systemWallet", "payment", "userWallet" }, allowSetters = true)
    private Set<WalletTransaction> walletTransactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemWallet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getSystemWalletID() {
        return this.systemWalletID;
    }

    public SystemWallet systemWalletID(UUID systemWalletID) {
        this.setSystemWalletID(systemWalletID);
        return this;
    }

    public void setSystemWalletID(UUID systemWalletID) {
        this.systemWalletID = systemWalletID;
    }

    public Double getBefore() {
        return this.before;
    }

    public SystemWallet before(Double before) {
        this.setBefore(before);
        return this;
    }

    public void setBefore(Double before) {
        this.before = before;
    }

    public Double getAmount() {
        return this.amount;
    }

    public SystemWallet amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCurrent() {
        return this.current;
    }

    public SystemWallet current(Double current) {
        this.setCurrent(current);
        return this;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getBlockAmount() {
        return this.blockAmount;
    }

    public SystemWallet blockAmount(Double blockAmount) {
        this.setBlockAmount(blockAmount);
        return this;
    }

    public void setBlockAmount(Double blockAmount) {
        this.blockAmount = blockAmount;
    }

    public Instant getMobifyDate() {
        return this.mobifyDate;
    }

    public SystemWallet mobifyDate(Instant mobifyDate) {
        this.setMobifyDate(mobifyDate);
        return this;
    }

    public void setMobifyDate(Instant mobifyDate) {
        this.mobifyDate = mobifyDate;
    }

    public Set<WalletTransaction> getWalletTransactions() {
        return this.walletTransactions;
    }

    public void setWalletTransactions(Set<WalletTransaction> walletTransactions) {
        if (this.walletTransactions != null) {
            this.walletTransactions.forEach(i -> i.setSystemWallet(null));
        }
        if (walletTransactions != null) {
            walletTransactions.forEach(i -> i.setSystemWallet(this));
        }
        this.walletTransactions = walletTransactions;
    }

    public SystemWallet walletTransactions(Set<WalletTransaction> walletTransactions) {
        this.setWalletTransactions(walletTransactions);
        return this;
    }

    public SystemWallet addWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransactions.add(walletTransaction);
        walletTransaction.setSystemWallet(this);
        return this;
    }

    public SystemWallet removeWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransactions.remove(walletTransaction);
        walletTransaction.setSystemWallet(null);
        return this;
    }

    public void increaseCurrent(Double amount) {
        if (this.current == null) this.current = 0.0;
        this.current += amount;
    }

    public SystemWallet addWalletTransactionAndUpdateBalance(WalletTransaction transaction) {
        boolean isDebit = false;

        if (transaction == null || transaction.getTransStatus() != TransactionStatus.SUCCESS) {
            return this;
        }

        transaction.setSystemWallet(this);
        this.walletTransactions.add(transaction);

        double amount = transaction.getAmount() != null ? transaction.getAmount() : 0.0;
        WalletTransactionType type = transaction.getWalletType();

        if (this.current == null) {
            this.current = 0.0;
        }

        // ✅ Lưu lại số dư trước giao dịch
        double beforeBalance = this.current;
        this.before = beforeBalance;
        transaction.setBefore(beforeBalance);

        switch (type) {
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
            case SYSTEM_GAIN_DEPOSIT:
                this.current += amount;
                break;
            case SYSTEM_REFUND_TO_PASSENGER:
                isDebit = true;
                this.current -= amount;
                break;
            case SYSTEM_REFUND_TO_DRIVER_DONE_TRIP:
                isDebit = true;
                this.current -= amount;
                break;
            default:
                // ❗Nếu type không liên quan hệ thống → không tác động số dư
                break;
        }

        double afterBalance = this.current;
        transaction.setCurrent(afterBalance);

        this.amount = isDebit ? -amount : amount;
        this.mobifyDate = transaction.getDate() != null ? transaction.getDate() : Instant.now();
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemWallet)) {
            return false;
        }
        return getId() != null && getId().equals(((SystemWallet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemWallet{" +
            "id=" + getId() +
            ", systemWalletID='" + getSystemWalletID() + "'" +
            ", before=" + getBefore() +
            ", amount=" + getAmount() +
            ", current=" + getCurrent() +
            ", blockAmount=" + getBlockAmount() +
            ", mobifyDate='" + getMobifyDate() + "'" +
            "}";
    }
}
