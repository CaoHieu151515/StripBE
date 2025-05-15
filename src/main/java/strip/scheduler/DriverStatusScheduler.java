package strip.scheduler;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Authority;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.enumeration.DriverStatus;
import strip.repository.AuthorityRepository;
import strip.repository.DriverRepository;
import strip.repository.UserRepository;
import strip.security.AuthoritiesConstants;

@Component
public class DriverStatusScheduler {

    private final Logger log = LoggerFactory.getLogger(DriverStatusScheduler.class);

    private final DriverRepository driverRepository;

    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    public DriverStatusScheduler(
        DriverRepository driverRepository,
        AuthorityRepository authorityRepository,
        UserRepository userRepository
    ) {
        this.driverRepository = driverRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
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

            User user = driver.getUser();
            removeDriverRoleIfPresent(user);
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

    private void removeDriverRoleIfPresent(User user) {
        Authority driverRole = authorityRepository.findById(AuthoritiesConstants.DRIVER).orElse(null);

        if (driverRole != null && user.getAuthorities().contains(driverRole)) {
            user.getAuthorities().remove(driverRole);
            userRepository.save(user);
        }
    }

    @Scheduled(fixedRate = 3600000) // mỗi giờ
    @Transactional
    public void unbanEligibleDrivers() {
        Instant now = Instant.now();

        List<Driver> bannedDrivers = driverRepository.findByDriverStatusAndBannedDayBefore(DriverStatus.BANNED, now);

        for (Driver driver : bannedDrivers) {
            if (driver.getExpirationDate() != null && driver.getExpirationDate().isAfter(now)) {
                driver.setDriverStatus(DriverStatus.ACTIVE);
                log.info("✅ Gỡ ban: tài xế {} có expiredDate hợp lệ đến {}", driver.getDriverID(), driver.getExpirationDate());
            } else {
                driver.setDriverStatus(DriverStatus.NOT_DRIVER);
                log.info("✅ Gỡ ban: tài xế {} nhưng không còn gói → NOT_DRIVER", driver.getDriverID());
            }

            driver.setBannedDay(null);
        }

        driverRepository.saveAll(bannedDrivers);
    }
}
