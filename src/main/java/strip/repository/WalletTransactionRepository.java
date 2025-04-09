package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import strip.domain.User;
import strip.domain.UserWallet;
import strip.domain.WalletTransaction;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;

/**
 * Spring Data JPA repository for the WalletTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findAllByUserWalletOrderByDateDesc(UserWallet userWallet);

    boolean existsByWalletTypeAndUserWallet_User_IdAndDateAfter(WalletTransactionType type, Long userId, Instant date);

    Page<WalletTransaction> findByWalletTypeInAndDateBetween(
        List<WalletTransactionType> walletTypes,
        Instant fromDate,
        Instant toDate,
        Pageable pageable
    );

    Optional<WalletTransaction> findFirstByUserWallet_UserAndWalletTypeAndAmountAndTransStatus(
        User user,
        WalletTransactionType walletType,
        Double amount,
        TransactionStatus transStatus
    );

    Optional<WalletTransaction> findByTransactionThirdPartyIDAndWalletTypeAndTransStatus(
        String transactionThirdPartyID,
        WalletTransactionType walletType,
        TransactionStatus transStatus
    );
}
