package strip.scheduler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Trip;
import strip.domain.enumeration.TripStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.TripRepository;
import strip.repository.WalletTransactionRepository;
import strip.service.TripCustomService;

@Component
public class TripPayoutScheduler {

    private final TripCustomService tripCustomService;

    private final Logger log = LoggerFactory.getLogger(TripPayoutScheduler.class);

    private final TripRepository tripRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public TripPayoutScheduler(
        TripCustomService tripCustomService,
        TripRepository tripRepository,
        WalletTransactionRepository walletTransactionRepository
    ) {
        this.tripCustomService = tripCustomService;
        this.tripRepository = tripRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    @Transactional
    public void payoutAfter24Hours() {
        Instant cutoff = Instant.now().minus(24, ChronoUnit.HOURS);

        List<Trip> trips = tripRepository.findByTripStatusAndEndDateBefore(TripStatus.DONE, cutoff);

        for (Trip trip : trips) {
            Long userid = trip.getDriver().getUser().getId();

            boolean alreadyPaid = walletTransactionRepository.existsByWalletTypeAndUserWallet_User_IdAndDateAfter(
                WalletTransactionType.DRIVER_DONE_TRIP_REFUND,
                userid,
                trip.getEndDate()
            );

            log.info("👉 alreadyPaid = {}", alreadyPaid);

            if (!alreadyPaid) {
                try {
                    tripCustomService.payoutToDriver(trip);
                    log.info("✅ Payout done for trip {}", trip.getTripID());
                } catch (Exception e) {
                    log.error("❌ Failed to payout for trip {}", trip.getTripID(), e);
                }
            }
        }
    }
}
