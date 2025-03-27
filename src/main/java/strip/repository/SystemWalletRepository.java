package strip.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.SystemWallet;

/**
 * Spring Data JPA repository for the SystemWallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemWalletRepository extends JpaRepository<SystemWallet, Long> {
    Optional<SystemWallet> findTopByOrderByMobifyDateDesc();
}
