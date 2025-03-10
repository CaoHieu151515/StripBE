package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.TripStopLocation;

/**
 * Spring Data JPA repository for the TripStopLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripStopLocationRepository extends JpaRepository<TripStopLocation, Long> {}
