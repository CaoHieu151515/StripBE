package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.Report;
import strip.domain.RequestTrip;
import strip.domain.enumeration.ReportStatus;
import strip.domain.enumeration.ReportType;

/**
 * Spring Data JPA repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByReportID(UUID requestTripID);

    @Query("select report from Report report where report.user.login = ?#{authentication.name}")
    List<Report> findByUserIsCurrentUser();

    @Query(
        """
            SELECT r FROM Report r
            WHERE (:status IS NULL OR r.reportStatus = :status)
              AND (:type IS NULL OR r.reportType = :type)
        """
    )
    Page<Report> findAllWithFilters(@Param("status") ReportStatus status, @Param("type") ReportType type, Pageable pageable);

    /**
     * Tìm theo trip nếu cần
     */
    Page<Report> findByTrip_TripID(UUID tripId, Pageable pageable);

    /**
     * Tìm theo driver
     */
    Page<Report> findByDriver_DriverID(UUID driverId, Pageable pageable);
}
