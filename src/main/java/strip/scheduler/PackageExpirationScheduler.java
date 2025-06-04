package strip.scheduler;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.DriverPackageSubscription;
import strip.repository.DriverPackageSubscriptionRepository;
import strip.repository.PackageDriverRepository;

@Component
public class PackageExpirationScheduler {

    private final Logger log = LoggerFactory.getLogger(PackageExpirationScheduler.class);

    private final PackageDriverRepository packageDriverRepository;

    private final DriverPackageSubscriptionRepository subscriptionRepository;

    public PackageExpirationScheduler(
        PackageDriverRepository packageDriverRepository,
        DriverPackageSubscriptionRepository subscriptionRepository
    ) {
        this.packageDriverRepository = packageDriverRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void expireOutdatedPackages() {
        Instant now = Instant.now();

        int updatedCount = packageDriverRepository.markPackagesAsExpired(now);

        if (updatedCount > 0) {
            log.info("✅ Đã đánh dấu {} gói package hết hạn (EXPIRED)", updatedCount);
        } else {
            // log.debug("ℹ️ Không có package nào hết hạn trong lần kiểm tra này.");
        }
    }

    @Scheduled(fixedRate = 3600000) // mỗi giờ
    @Transactional
    public void deactivateExpiredSubscriptions() {
        Instant now = Instant.now();
        List<DriverPackageSubscription> expiredSubs = subscriptionRepository.findByActiveTrueAndExpirationDateBefore(now);

        for (DriverPackageSubscription sub : expiredSubs) {
            sub.setActive(false);
        }

        if (!expiredSubs.isEmpty()) {
            subscriptionRepository.saveAll(expiredSubs);
        }
    }
}
