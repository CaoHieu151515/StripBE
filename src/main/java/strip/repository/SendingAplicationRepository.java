package strip.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.SendingAplication;

/**
 * Spring Data JPA repository for the SendingAplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SendingAplicationRepository extends JpaRepository<SendingAplication, Long> {
    @Query("select sendingAplication from SendingAplication sendingAplication where sendingAplication.user.login = ?#{authentication.name}")
    List<SendingAplication> findByUserIsCurrentUser();
}
