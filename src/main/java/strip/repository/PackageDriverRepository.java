package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.PackageDriver;

/**
 * Spring Data JPA repository for the PackageDriver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageDriverRepository extends JpaRepository<PackageDriver, Long> {}
