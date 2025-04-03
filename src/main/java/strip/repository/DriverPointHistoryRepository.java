package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.DriverPointHistory;

/**
 * Spring Data JPA repository for the DriverPointHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverPointHistoryRepository extends JpaRepository<DriverPointHistory, Long> {
    Optional<DriverPointHistory> findByPointId(UUID driverID);

    Page<DriverPointHistory> findByUserDetail_AppUserDetail(UUID userDetailId, Pageable pageable);
}
