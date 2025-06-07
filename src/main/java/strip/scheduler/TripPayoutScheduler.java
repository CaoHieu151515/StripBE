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
import strip.domain.WalletTransaction;
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.TransactionStatus;
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
            // String tripId = trip.getTripID().toString();

            boolean alreadyPaid = walletTransactionRepository.existsByWalletTypeAndTransactionThirdPartyIDAndTransStatus(
                WalletTransactionType.DRIVER_DONE_TRIP_REFUND,
                trip.getTripID().toString(),
                TransactionStatus.SUCCESS
            );

            // log.info("👉 alreadyPaid = {} for trip {}", alreadyPaid, tripId);

            if (!alreadyPaid) {
                try {
                    tripCustomService.payoutToDriver(trip);
                    // log.info("✅ Payout done for trip {}", tripId);
                } catch (Exception e) {
                    // log.error("❌ Failed to payout for trip {}", tripId, e);
                }
            } else {
                // log.info("⚠ Trip {} already paid — skipping", tripId);
            }
        }
    }

    // @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    // @Transactional
    // public void autoCompleteOngoingTrips() {
    // Instant cutoff = Instant.now().minus(2, ChronoUnit.HOURS);

    // List<Trip> trips =
    // tripRepository.findByTripStatusAndEndDateBefore(TripStatus.ON_GOING, cutoff);

    // for (Trip trip : trips) {
    // try {
    // trip.setTripStatus(TripStatus.DONE);
    // tripRepository.save(trip);
    // // log.info("✅ Auto-completed trip {} -> DONE", trip.getTripID());
    // } catch (Exception e) {
    // // log.error("❌ Failed to complete trip {}: {}", trip.getTripID(),
    // e.getMessage(), e);
    // }
    // }
    // }

    // @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    // @Transactional
    // public void autoCompletePassengerRequestInDoneTrips() {
    // List<Trip> doneTrips = tripRepository.findByTripStatus(TripStatus.DONE);

    // for (Trip trip : doneTrips) {
    // try {
    // int updated = requestTripRepository.updateStatusByTripAndCurrentStatus(
    // trip.getTripID(),
    // PassengerStatus.BOOKED,
    // PassengerStatus.DONE
    // );

    // if (updated > 0) {
    // // log.info("✅ Updated {} passenger(s) from BOOKED → DONE for trip {}",
    // updated, trip.getTripID());
    // }
    // } catch (Exception e) {
    // // log.error("❌ Failed to update passenger status for trip {}: {}",
    // trip.getTripID(), e.getMessage(), e);
    // }
    // }
    // }

    @Scheduled(fixedRate = 3600000) // chạy mỗi giờ
    @Transactional
    public void autoCompleteTripsAndPassengers() {
        // ✅ B1: Hoàn tất chuyến ON_GOING → DONE nếu đã quá 2h
        Instant cutoff = Instant.now().minus(2, ChronoUnit.HOURS);
        List<Trip> tripsToComplete = tripRepository.findByTripStatusAndEndDateBefore(TripStatus.ON_GOING, cutoff);

        for (Trip trip : tripsToComplete) {
            try {
                trip.setTripStatus(TripStatus.DONE);
                tripRepository.save(trip);
                // log.info("✅ Auto-completed trip {} → DONE", trip.getTripID());
            } catch (Exception e) {
                // log.error("❌ Failed to complete trip {}: {}", trip.getTripID(),
                // e.getMessage(), e);
            }
        }

        // ✅ B2: Cập nhật các passenger BOOKED → DONE trong các trip đã DONE
        List<Trip> doneTrips = tripRepository.findByTripStatus(TripStatus.DONE);

        for (Trip trip : doneTrips) {
            try {
                int updated = requestTripRepository.updateStatusByTripAndCurrentStatus(
                    trip.getTripID(),
                    PassengerStatus.BOOKED,
                    PassengerStatus.DONE
                );

                if (updated > 0) {
                    // log.info("✅ Updated {} passenger(s) BOOKED → DONE in trip {}", updated,
                    // trip.getTripID());
                }
            } catch (Exception e) {
                // log.error("❌ Failed to update passenger status for trip {}: {}",
                // trip.getTripID(), e.getMessage(), e);
            }
        }
    }

    @Scheduled(fixedRate = 3600000) // mỗi 1 giờ
    @Transactional
    public void autoUpdateTransactionsForDoneTrips() {
        List<Trip> doneTrips = tripRepository.findByTripStatus(TripStatus.DONE);

        int totalUpdated = 0;

        for (Trip trip : doneTrips) {
            String tripId = trip.getTripID().toString();

            List<WalletTransaction> pendingTransactions = walletTransactionRepository.findByTransactionThirdPartyIDAndTransStatus(
                tripId,
                TransactionStatus.PENDING
            );

            if (!pendingTransactions.isEmpty()) {
                for (WalletTransaction tx : pendingTransactions) {
                    tx.setTransStatus(TransactionStatus.SUCCESS);
                }

                walletTransactionRepository.saveAll(pendingTransactions);

                totalUpdated += pendingTransactions.size();
                log.info("✅ Đã cập nhật {} giao dịch → SUCCESS cho trip {}", pendingTransactions.size(), tripId);
            }
        }

        if (totalUpdated == 0) {
            log.info("✅ Không có giao dịch PENDING nào cần cập nhật");
        } else {
            log.info("✅ Tổng số giao dịch đã cập nhật: {}", totalUpdated);
        }
    }
}
