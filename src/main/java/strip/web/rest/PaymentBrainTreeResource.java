package strip.web.rest;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strip.service.PaymentBrainTreeService;
import strip.service.dto.PaymentRequestDTO;

@RestController
@RequestMapping("/api/payment/braintree") // 👉 thêm namespace cho rõ
public class PaymentBrainTreeResource {

    private final PaymentBrainTreeService paymentBrainTreeService;

    public PaymentBrainTreeResource(PaymentBrainTreeService paymentBrainTreeService) {
        this.paymentBrainTreeService = paymentBrainTreeService;
    }

    @GetMapping("/client-token")
    public ResponseEntity<String> getClientToken() {
        return ResponseEntity.ok(paymentBrainTreeService.generateClientToken());
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@ModelAttribute PaymentRequestDTO payload) {
        String nonce = payload.getNonce();
        BigDecimal amount = payload.getAmount();

        System.out.println("🧾 Received nonce = " + nonce);
        System.out.println("💰 Amount = " + amount);

        Result<Transaction> result = paymentBrainTreeService.checkout(nonce, amount);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getTarget().getId());
        } else {
            return ResponseEntity.status(500).body("Transaction failed: " + result.getMessage());
        }
    }
}
