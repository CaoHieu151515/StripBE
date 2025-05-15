package strip.scheduler;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import strip.repository.PackageDriverRepository;

@Component
public class PackageExpirationScheduler {

    private final Logger log = LoggerFactory.getLogger(PackageExpirationScheduler.class);

    private final PackageDriverRepository packageDriverRepository;

    public PackageExpirationScheduler(PackageDriverRepository packageDriverRepository) {
        this.packageDriverRepository = packageDriverRepository;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void expireOutdatedPackages() {
        Instant now = Instant.now();

        int updatedCount = packageDriverRepository.markPackagesAsExpired(now);

        if (updatedCount > 0) {
            log.info("✅ Đã đánh dấu {} gói package hết hạn (EXPIRED)", updatedCount);
        } else {
            log.debug("ℹ️ Không có package nào hết hạn trong lần kiểm tra này.");
        }
    }
}
