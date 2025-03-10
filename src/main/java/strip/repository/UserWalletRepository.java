package strip.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.UserWallet;

/**
 * Spring Data JPA repository for the UserWallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {}
