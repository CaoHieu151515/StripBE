package strip.service.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {

    private String nonce;
    private BigDecimal amount;

    public PaymentRequestDTO() {}

    public PaymentRequestDTO(String nonce, BigDecimal amount) {
        this.nonce = nonce;
        this.amount = amount;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
