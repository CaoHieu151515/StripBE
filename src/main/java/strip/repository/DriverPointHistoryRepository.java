package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.DriverPointHistory;

/**
 * Spring Data JPA repository for the DriverPointHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverPointHistoryRepository extends JpaRepository<DriverPointHistory, Long> {}
