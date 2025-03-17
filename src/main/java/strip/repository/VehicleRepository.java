package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import strip.domain.Vehicle;
import strip.domain.enumeration.VehicleStatus;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(VehicleStatus vehicleStatus);

    Optional<Vehicle> findByVehicleID(UUID vehicleID);
}
