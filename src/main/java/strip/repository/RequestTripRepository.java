package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.RequestTrip;
import strip.domain.Trip;

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
}
