package strip.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.PackageDriver;
import strip.domain.enumeration.PackageDriverStatus;

/**
 * Spring Data JPA repository for the PackageDriver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageDriverRepository extends JpaRepository<PackageDriver, Long> {
    List<PackageDriver> findByStatus(PackageDriverStatus status);
}
