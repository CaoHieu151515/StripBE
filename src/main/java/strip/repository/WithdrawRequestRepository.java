package strip.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.WithdrawRequest;

/**
 * Spring Data JPA repository for the WithdrawRequest entity.
 */
@Repository
public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequest, Long> {
    // Lấy danh sách các yêu cầu rút tiền theo username
    @Query("SELECT w FROM WithdrawRequest w WHERE w.user.login = :login ORDER BY w.requestDate DESC")
    List<WithdrawRequest> findAllByUserLogin(@Param("login") String login);

    // Tùy chọn: lấy theo UUID nếu cần
    Optional<WithdrawRequest> findByUuid(java.util.UUID uuid);
}
