package strip.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import strip.config.ApplicationProperties;

@Service
public class PaypalPayoutService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ApplicationProperties applicationProperties;

    public PaypalPayoutService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getAccessToken() {
        String clientId = applicationProperties.getPaypal().getClientId();
        String secret = applicationProperties.getPaypal().getSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, secret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(
            "https://api-m.sandbox.paypal.com/v1/oauth2/token",
            request,
            JsonNode.class
        );

        return response.getBody().get("access_token").asText();
    }

    public String payout(String email, double amount, String currency, String note) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
            "sender_batch_header",
            Map.of("sender_batch_id", UUID.randomUUID().toString(), "email_subject", "S-Trip đã gửi tiền cho bạn"),
            "items",
            List.of(
                Map.of(
                    "recipient_type",
                    "EMAIL",
                    "amount",
                    Map.of("value", String.format("%.2f", amount), "currency", currency),
                    "note",
                    note,
                    "receiver",
                    email,
                    "sender_item_id",
                    UUID.randomUUID().toString()
                )
            )
        );

        HttpEntity<?> request = new HttpEntity<>(payload, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(
            "https://api-m.sandbox.paypal.com/v1/payments/payouts",
            request,
            JsonNode.class
        );

        return response.getBody().get("batch_header").get("payout_batch_id").asText();
    }
}
