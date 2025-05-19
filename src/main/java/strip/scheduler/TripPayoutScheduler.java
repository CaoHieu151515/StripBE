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
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.TripStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.RequestTripRepository;
import strip.repository.TripRepository;
import strip.repository.WalletTransactionRepository;
import strip.service.TripCustomService;

@Component
public class TripPayoutScheduler {

    private final TripCustomService tripCustomService;

    private final Logger log = LoggerFactory.getLogger(TripPayoutScheduler.class);

    private final TripRepository tripRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final RequestTripRepository requestTripRepository;

    public TripPayoutScheduler(
        TripCustomService tripCustomService,
        TripRepository tripRepository,
        WalletTransactionRepository walletTransactionRepository,
        RequestTripRepository requestTripRepository
    ) {
        this.tripCustomService = tripCustomService;
        this.tripRepository = tripRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.requestTripRepository = requestTripRepository;
    }

    @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    @Transactional
    public void payoutAfter24Hours() {
        Instant cutoff = Instant.now().minus(8, ChronoUnit.HOURS);

        List<Trip> trips = tripRepository.findByTripStatusAndEndDateBefore(TripStatus.DONE, cutoff);

        for (Trip trip : trips) {
            String tripId = trip.getTripID().toString();

            boolean alreadyPaid = walletTransactionRepository.existsByWalletTypeAndTransactionThirdPartyID(
                WalletTransactionType.DRIVER_DONE_TRIP_REFUND,
                tripId
            );

            log.info("👉 alreadyPaid = {} for trip {}", alreadyPaid, tripId);

            if (!alreadyPaid) {
                try {
                    tripCustomService.payoutToDriver(trip);
                    log.info("✅ Payout done for trip {}", tripId);
                } catch (Exception e) {
                    log.error("❌ Failed to payout for trip {}", tripId, e);
                }
            } else {
                log.info("⚠ Trip {} already paid — skipping", tripId);
            }
        }
    }

    @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    @Transactional
    public void autoCompleteOngoingTrips() {
        Instant cutoff = Instant.now().minus(2, ChronoUnit.HOURS);

        List<Trip> trips = tripRepository.findByTripStatusAndEndDateBefore(TripStatus.ON_GOING, cutoff);

        for (Trip trip : trips) {
            try {
                trip.setTripStatus(TripStatus.DONE);
                tripRepository.save(trip);
                log.info("✅ Auto-completed trip {} -> DONE", trip.getTripID());
            } catch (Exception e) {
                log.error("❌ Failed to complete trip {}: {}", trip.getTripID(), e.getMessage(), e);
            }
        }
    }

    @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    @Transactional
    public void autoCompletePassengerRequestInDoneTrips() {
        List<Trip> doneTrips = tripRepository.findByTripStatus(TripStatus.DONE);

        for (Trip trip : doneTrips) {
            try {
                int updated = requestTripRepository.updateStatusByTripAndCurrentStatus(
                    trip.getTripID(),
                    PassengerStatus.BOOKED,
                    PassengerStatus.DONE
                );

                if (updated > 0) {
                    log.info("✅ Updated {} passenger(s) from BOOKED → DONE for trip {}", updated, trip.getTripID());
                }
            } catch (Exception e) {
                log.error("❌ Failed to update passenger status for trip {}: {}", trip.getTripID(), e.getMessage(), e);
            }
        }
    }
}
