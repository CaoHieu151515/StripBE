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
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;

/**
 * A WalletTransaction.
 */
@Entity
@Table(name = "wallet_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "trans_id", length = 36)
    private UUID transID;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type")
    private WalletTransactionType walletType;

    @Enumerated(EnumType.STRING)
    @Column(name = "trans_status")
    private TransactionStatus transStatus;

    @Column(name = "transaction_third_party_id")
    private String transactionThirdPartyID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "walletTransactions" }, allowSetters = true)
    private SystemWallet systemWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "packageDriver", "walletTransactions" }, allowSetters = true)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "walletTransactions", "walletDeposits" }, allowSetters = true)
    private UserWallet userWallet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WalletTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTransID() {
        return this.transID;
    }

    public WalletTransaction transID(UUID transID) {
        this.setTransID(transID);
        return this;
    }

    public void setTransID(UUID transID) {
        this.transID = transID;
    }

    public Double getAmount() {
        return this.amount;
    }

    public WalletTransaction amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDate() {
        return this.date;
    }

    public WalletTransaction date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public WalletTransactionType getWalletType() {
        return this.walletType;
    }

    public WalletTransaction walletType(WalletTransactionType walletType) {
        this.setWalletType(walletType);
        return this;
    }

    public void setWalletType(WalletTransactionType walletType) {
        this.walletType = walletType;
    }

    public TransactionStatus getTransStatus() {
        return this.transStatus;
    }

    public WalletTransaction transStatus(TransactionStatus transStatus) {
        this.setTransStatus(transStatus);
        return this;
    }

    public void setTransStatus(TransactionStatus transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransactionThirdPartyID() {
        return this.transactionThirdPartyID;
    }

    public WalletTransaction transactionThirdPartyID(String transactionThirdPartyID) {
        this.setTransactionThirdPartyID(transactionThirdPartyID);
        return this;
    }

    public void setTransactionThirdPartyID(String transactionThirdPartyID) {
        this.transactionThirdPartyID = transactionThirdPartyID;
    }

    public SystemWallet getSystemWallet() {
        return this.systemWallet;
    }

    public void setSystemWallet(SystemWallet systemWallet) {
        this.systemWallet = systemWallet;
    }

    public WalletTransaction systemWallet(SystemWallet systemWallet) {
        this.setSystemWallet(systemWallet);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public WalletTransaction payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public UserWallet getUserWallet() {
        return this.userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public WalletTransaction userWallet(UserWallet userWallet) {
        this.setUserWallet(userWallet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((WalletTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletTransaction{" +
            "id=" + getId() +
            ", transID='" + getTransID() + "'" +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", walletType='" + getWalletType() + "'" +
            ", transStatus='" + getTransStatus() + "'" +
            ", transactionThirdPartyID='" + getTransactionThirdPartyID() + "'" +
            "}";
    }
}
