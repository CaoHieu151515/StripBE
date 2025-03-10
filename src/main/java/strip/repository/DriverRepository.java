package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.Driver;

/**
 * Spring Data JPA repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {}
