package strip.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import strip.domain.User;
import strip.domain.UserDetail;

/**
 * Spring Data JPA repository for the UserDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUser(User user);
    Optional<UserDetail> findByUserId(Long userId);
    Optional<UserDetail> findByAppUserDetail(UUID userId);
}
