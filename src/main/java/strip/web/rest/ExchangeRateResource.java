package strip.web.rest;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import strip.service.ExchangeRateService;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeRateResource {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateResource(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Hidden
    @PostMapping("/fetch-usd-vnd")
    public ResponseEntity<Void> fetchUsdToVndRate() {
        exchangeRateService.fetchAndSaveUsdToVndOncePerDay();
        return ResponseEntity.ok().build();
    }
}
