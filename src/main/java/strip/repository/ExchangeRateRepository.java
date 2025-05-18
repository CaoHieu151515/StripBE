package strip.repository;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import strip.domain.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Instant> {
    Optional<ExchangeRate> findTopByFromCurrencyAndToCurrencyOrderByDateDesc(String from, String to);

    boolean existsByDateBetween(Instant start, Instant end);
}
