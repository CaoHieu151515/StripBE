package strip.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.Passenger;

/**
 * Spring Data JPA repository for the Passenger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query("select passenger from Passenger passenger where passenger.user.login = ?#{authentication.name}")
    List<Passenger> findByUserIsCurrentUser();
}
