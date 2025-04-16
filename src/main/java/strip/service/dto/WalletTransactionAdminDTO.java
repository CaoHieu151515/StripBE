package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;

public class WalletTransactionAdminDTO {

    private UUID transactionId;
    private WalletTransactionType type;
    private TransactionStatus status;
    private Double amount;
    private String fromOwner;
    private String toOwner;
    private Instant createdDate;
    private String description;

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromOwner() {
        return fromOwner;
    }

    public void setFromOwner(String fromOwner) {
        this.fromOwner = fromOwner;
    }

    public String getToOwner() {
        return toOwner;
    }

    public void setToOwner(String toOwner) {
        this.toOwner = toOwner;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WalletTransactionType getType() {
        return type;
    }

    public void setType(WalletTransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    // getters/setters

}
