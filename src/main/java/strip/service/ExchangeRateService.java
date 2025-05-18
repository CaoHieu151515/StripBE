package strip.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import strip.domain.ExchangeRate;
import strip.repository.ExchangeRateRepository;

@Service
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public void fetchAndSaveUsdToVndOncePerDay() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        boolean exists = exchangeRateRepository.existsByDateBetween(
            today.atStartOfDay().toInstant(ZoneOffset.UTC),
            today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );

        if (exists) {
            log.info("✅ Tỷ giá USD→VND cho hôm nay đã tồn tại, không gọi lại API.");
            return;
        }

        String url = "https://api.apilayer.com/exchangerates_data/convert?from=USD&to=VND&amount=1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "8sdXX47aMdDXJXkkt63OrFoYFIw7XWuy");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class);
        JsonNode body = response.getBody();

        JsonNode rateNode = body.path("result");

        if (rateNode.isMissingNode() || !rateNode.isNumber()) {
            log.warn("⚠ Không tìm thấy tỷ giá USD→VND trong phản hồi API");
            return;
        }

        double rate = rateNode.asDouble();

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDate(Instant.now());
        exchangeRate.setFromCurrency("USD");
        exchangeRate.setToCurrency("VND");
        exchangeRate.setRate(rate);

        exchangeRateRepository.save(exchangeRate);

        log.info("💱 Đã lưu tỷ giá USD→VND hôm nay = {}", rate);
    }
}
