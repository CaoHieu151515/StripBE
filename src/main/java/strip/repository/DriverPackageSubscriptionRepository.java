package strip.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.DriverPackageSubscription;

/**
 * Spring Data JPA repository for the DriverPackageSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverPackageSubscriptionRepository extends JpaRepository<DriverPackageSubscription, UUID> {}
