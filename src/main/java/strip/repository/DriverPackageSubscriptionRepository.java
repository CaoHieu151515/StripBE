package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.DriverPackageSubscription;

/**
 * Spring Data JPA repository for the DriverPackageSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverPackageSubscriptionRepository extends JpaRepository<DriverPackageSubscription, UUID> {
    @Query(
        "SELECT s.packageDriver.packageID, s.packageDriver.name, COUNT(s) " +
        "FROM DriverPackageSubscription s " +
        "WHERE s.purchaseDate BETWEEN :from AND :to " +
        "GROUP BY s.packageDriver.packageID, s.packageDriver.name"
    )
    List<Object[]> countPackageSales(@Param("from") Instant from, @Param("to") Instant to);

    @Query(
        "SELECT s.packageDriver.packageID, s.packageDriver.name, COUNT(s), FUNCTION('DATE', s.purchaseDate) " +
        "FROM DriverPackageSubscription s " +
        "WHERE s.purchaseDate BETWEEN :from AND :to " +
        "GROUP BY s.packageDriver.packageID, s.packageDriver.name, FUNCTION('DATE', s.purchaseDate)"
    )
    List<Object[]> countPackageSalesWithDate(@Param("from") Instant from, @Param("to") Instant to);

    @Query(
        "SELECT DISTINCT s.packageDriver.packageID, s.packageDriver.name " +
        "FROM DriverPackageSubscription s " +
        "WHERE s.purchaseDate BETWEEN :from AND :to"
    )
    List<Object[]> findPackagesInTimeRange(@Param("from") Instant from, @Param("to") Instant to);
}
