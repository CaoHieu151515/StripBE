package strip.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.TripStopLocation;

/**
 * Spring Data JPA repository for the TripStopLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripStopLocationRepository extends JpaRepository<TripStopLocation, Long> {
    @Transactional
    void deleteAllByTrip_TripID(UUID tripId);

    Optional<TripStopLocation> findByStopLocaID(UUID stopLocaId);
}
