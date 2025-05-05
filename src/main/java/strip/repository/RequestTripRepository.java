package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.RequestTrip;
import strip.domain.Trip;
import strip.domain.User;
import strip.domain.enumeration.PassengerStatus;

/**
 * Spring Data JPA repository for the RequestTrip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestTripRepository extends JpaRepository<RequestTrip, Long> {
    @Query("select requestTrip from RequestTrip requestTrip where requestTrip.user.login = ?#{authentication.name}")
    List<RequestTrip> findByUserIsCurrentUser();

    List<RequestTrip> findAllByTrip_TripID(UUID tripId);

    Optional<RequestTrip> findByRequestTripID(UUID requestTripID);

    List<RequestTrip> findByUserAndStatus(User user, PassengerStatus status);

    List<RequestTrip> findByTripAndStatus(Trip trip, PassengerStatus status);

    boolean existsByTripAndUser(Trip trip, User user);

    @Query("SELECT COUNT(r) > 0 FROM RequestTrip r WHERE r.trip = :trip AND r.user = :user AND r.status NOT IN ('CANCEL', 'REJECTED')")
    boolean hasJoinedActiveTrip(@Param("trip") Trip trip, @Param("user") User user);

    List<RequestTrip> findByTrip_TripIDAndUser_Id(UUID tripId, Long userId);
}
