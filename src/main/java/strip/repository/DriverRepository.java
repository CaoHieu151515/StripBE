package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.enumeration.DriverStatus;

/**
 * Spring Data JPA repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUser(User user);

    List<Driver> findByUsedtoDriverFalseAndDriverStatus(DriverStatus driverStatus);

    Optional<Driver> findByDriverID(UUID driverID);
}
