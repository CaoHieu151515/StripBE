package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.Driver;
import strip.domain.Rating;
import strip.domain.Trip;
import strip.domain.User;
import strip.domain.enumeration.RatingType;

/**
 * Spring Data JPA repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("select rating from Rating rating where rating.user.login = ?#{authentication.name}")
    List<Rating> findByUserIsCurrentUser();

    Optional<Rating> findByTripAndDriverAndUser(Trip trip, Driver driver, User user);

    @Query("SELECT AVG(r.ratingDriver) FROM Rating r WHERE r.driver.driverID = :driverId")
    Double findAverageRatingByDriverId(@Param("driverId") UUID driverId);

    List<Rating> findByDriver_DriverIDAndRatingType(UUID driverId, RatingType type);
}
