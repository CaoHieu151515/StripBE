package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
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

    @Query(
        """
            SELECT t FROM Trip t
            WHERE (:startLocation IS NULL OR LOWER(t.startLocation) LIKE LOWER(CONCAT('%', :startLocation, '%')))
              AND (:endLocation IS NULL OR LOWER(t.endLocation) LIKE LOWER(CONCAT('%', :endLocation, '%')))
              AND (:status IS NULL OR t.tripStatus = :status)
              AND (:driverId IS NULL OR t.driver.driverID = :driverId)
              AND (:tripId IS NULL OR t.id = :tripId)
        """
    )
    Page<Trip> findAllWithFilters(
        @Param("startLocation") String startLocation,
        @Param("endLocation") String endLocation,
        @Param("status") TripStatus status,
        @Param("driverId") UUID driverId,
        @Param("tripId") Long tripId,
        Pageable pageable
    );

    @EntityGraph(attributePaths = { "driver.user", "vehicle", "tripStopLocations" })
    @Query("SELECT t FROM Trip t WHERE t.tripID = :id")
    Optional<Trip> findByIdWithRelations(@Param("id") UUID id);

    List<Trip> findByTripStatusAndEndDateBefore(TripStatus status, Instant before);

    Page<Trip> findByDriver_User_LoginAndTripStatusIn(String login, List<TripStatus> statuses, Pageable pageable);

    @Query("SELECT t FROM Trip t LEFT JOIN FETCH t.tripStopLocations WHERE t.tripID = :tripId")
    Optional<Trip> findByTripIDWithStops(@Param("tripId") UUID tripId);

    boolean existsByTripID(UUID tripId);
}
