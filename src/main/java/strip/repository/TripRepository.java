package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.Trip;
import strip.domain.enumeration.TripStatus;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT t FROM Trip t WHERE t.driver.driverID = :driverId " + "AND t.startDate <= :endDate AND t.endDate >= :startDate")
    List<Trip> findOverlappingTripsByDriver(
        @Param("driverId") UUID driverId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    Optional<Trip> findByTripID(UUID tripId);

    @Query(
        "SELECT t FROM Trip t " +
        "LEFT JOIN FETCH t.driver " +
        "LEFT JOIN FETCH t.vehicle " +
        "LEFT JOIN FETCH t.tripStopLocations " +
        "WHERE t.tripID = :tripId"
    )
    Optional<Trip> findFullTripByTripID(@Param("tripId") UUID tripId);

    @Query("SELECT t FROM Trip t WHERE t.tripStatus IN (:statuses) AND t.currentSeat < t.maxSeat ORDER BY t.startDate ASC")
    List<Trip> findAvailableTrips(@Param("statuses") List<TripStatus> statuses);
}
