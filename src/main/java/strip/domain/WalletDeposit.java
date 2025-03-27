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
import strip.domain.enumeration.PaymentStatus;

/**
 * A WalletDeposit.
 */
@Entity
@Table(name = "wallet_deposit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "bank_number")
    private String bankNumber;

    @Column(name = "name_of_bank")
    private String nameOfBank;

    @Column(name = "bank")
    private String bank;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "walletTransactions", "walletDeposits" }, allowSetters = true)
    private UserWallet userWallet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public WalletDeposit id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBankNumber() {
        return this.bankNumber;
    }

    public WalletDeposit bankNumber(String bankNumber) {
        this.setBankNumber(bankNumber);
        return this;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getNameOfBank() {
        return this.nameOfBank;
    }

    public WalletDeposit nameOfBank(String nameOfBank) {
        this.setNameOfBank(nameOfBank);
        return this;
    }

    public void setNameOfBank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public String getBank() {
        return this.bank;
    }

    public WalletDeposit bank(String bank) {
        this.setBank(bank);
        return this;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Double getAmount() {
        return this.amount;
    }

    public WalletDeposit amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDate() {
        return this.date;
    }

    public WalletDeposit date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public WalletDeposit status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public UserWallet getUserWallet() {
        return this.userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public WalletDeposit userWallet(UserWallet userWallet) {
        this.setUserWallet(userWallet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletDeposit)) {
            return false;
        }
        return getId() != null && getId().equals(((WalletDeposit) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletDeposit{" +
            "id=" + getId() +
            ", bankNumber='" + getBankNumber() + "'" +
            ", nameOfBank='" + getNameOfBank() + "'" +
            ", bank='" + getBank() + "'" +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
