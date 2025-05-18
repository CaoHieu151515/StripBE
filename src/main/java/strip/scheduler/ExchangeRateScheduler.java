package strip.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import strip.service.ExchangeRateService;

@Component
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateScheduler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Scheduled(cron = "0 0 6 * * ?") // mỗi ngày lúc 6h sáng
    public void updateRateDaily() {
        exchangeRateService.fetchAndSaveUsdToVndOncePerDay();
    }
}
