package strip.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.User;
import strip.domain.UserWallet;

/**
 * Spring Data JPA repository for the UserWallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
    Optional<UserWallet> findByUser(User user);

    Optional<UserWallet> findByUser_Id(Long userId);
}
