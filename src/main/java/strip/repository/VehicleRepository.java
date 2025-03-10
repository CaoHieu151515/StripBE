package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.Vehicle;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {}
