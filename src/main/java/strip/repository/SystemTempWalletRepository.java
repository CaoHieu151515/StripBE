package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.SystemTempWallet;

/**
 * Spring Data JPA repository for the SystemTempWallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemTempWalletRepository extends JpaRepository<SystemTempWallet, Long> {}
