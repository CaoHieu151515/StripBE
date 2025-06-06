package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import strip.domain.Driver;
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

    Optional<Vehicle> findFirstByDriverAndStatus(Driver driver, VehicleStatus status);

    Optional<Vehicle> findFirstByDriver_DriverIDAndStatus(UUID driverId, VehicleStatus status);

    Optional<Vehicle> findByDriver(Driver driver);

    List<Vehicle> findAllByDriver(Driver driver);

    boolean existsByDriverAndStatus(Driver driver, VehicleStatus status);
}
