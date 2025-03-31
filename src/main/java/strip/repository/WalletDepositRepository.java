package strip.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import strip.domain.User;
import strip.domain.WalletDeposit;
import strip.domain.enumeration.PaymentStatus;

@Repository
public interface WalletDepositRepository extends JpaRepository<WalletDeposit, UUID> {
    // Tìm theo trạng thái (PENDING, SUCCESS, FAILED...)
    List<WalletDeposit> findByStatus(PaymentStatus status);

    // Tìm theo người dùng (User)
    List<WalletDeposit> findByUserWallet_User(User user);
}
