package strip.scheduler;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import strip.domain.Driver;
import strip.domain.Driver_;
import strip.domain.enumeration.DriverStatus;
import strip.repository.DriverRepository;

@Component
public class DriverStatusScheduler {

    private final DriverRepository driverRepository;
    private final Logger log = LoggerFactory.getLogger(DriverStatusScheduler.class);

    public DriverStatusScheduler(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    /**
     * Chạy mỗi ngày lúc 01:00 UTC để kiểm tra và cập nhật trạng thái tài xế hết hạn
     */
    @Scheduled(cron = "0 0 1 * * *", zone = "UTC")
    public void deactivateExpiredDrivers() {
        Instant now = Instant.now();
        List<Driver> expiredDrivers = driverRepository.findAllByDriverStatusAndExpirationDateBefore(DriverStatus.ACTIVE, now);

        for (Driver driver : expiredDrivers) {
            driver.setDriverStatus(DriverStatus.NOT_DRIVER);
        }

        if (!expiredDrivers.isEmpty()) {
            driverRepository.saveAll(expiredDrivers);
            System.out.println("✔️ Đã cập nhật " + expiredDrivers.size() + " tài xế hết hạn về NOT_DRIVER.");
        }
    }

    // từ động cấp 14 điểm mỗi tháng
    @Scheduled(cron = "0 0 0 1 * ?", zone = "UTC")
    public void resetDriverPoints() {
        List<Driver> allDrivers = driverRepository.findAll();

        for (Driver driver : allDrivers) {
            driver.setDriverPoint(14);
        }

        if (!allDrivers.isEmpty()) {
            driverRepository.saveAll(allDrivers);
            log.info("Đã reset driverPoint về 14 cho {} tài xế", allDrivers.size());
        }
    }
}
