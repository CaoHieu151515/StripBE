package strip.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.RequestTrip;

/**
 * Spring Data JPA repository for the RequestTrip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestTripRepository extends JpaRepository<RequestTrip, Long> {
    @Query("select requestTrip from RequestTrip requestTrip where requestTrip.user.login = ?#{authentication.name}")
    List<RequestTrip> findByUserIsCurrentUser();
}
