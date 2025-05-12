package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(
        "SELECT t FROM WalletTransaction t " +
        "WHERE (:walletType IS NULL OR t.walletType = :walletType) " +
        "AND (:walletStatus IS NULL OR t.transStatus = :walletStatus) " +
        "AND (:fromDate IS NULL OR t.date >= :fromDate) " +
        "AND (:toDate IS NULL OR t.date <= :toDate)"
    )
    Page<WalletTransaction> searchWalletTransactions(
        @Param("walletType") WalletTransactionType walletType,
        @Param("walletStatus") TransactionStatus walletStatus,
        @Param("fromDate") Instant fromDate,
        @Param("toDate") Instant toDate,
        Pageable pageable
    );

    @Query(
        "SELECT SUM(wt.amount), wt.walletType, FUNCTION('DATE', wt.date) " +
        "FROM WalletTransaction wt " +
        "WHERE wt.transStatus = 'SUCCESS' " +
        "AND wt.walletType IN :incomeTypes " +
        "AND wt.date BETWEEN :from AND :to " +
        "GROUP BY wt.walletType, FUNCTION('DATE', wt.date)"
    )
    List<Object[]> sumProfitByTypeAndDate(
        @Param("incomeTypes") List<WalletTransactionType> incomeTypes,
        @Param("from") Instant from,
        @Param("to") Instant to
    );

    @Query(
        "SELECT COUNT(wt), FUNCTION('DATE', wt.date) " +
        "FROM WalletTransaction wt " +
        "WHERE wt.transStatus = 'SUCCESS' " +
        "AND wt.walletType = :walletType " +
        "AND wt.date BETWEEN :from AND :to " +
        "GROUP BY FUNCTION('DATE', wt.date)"
    )
    List<Object[]> countTripCreatesByDate(
        @Param("walletType") WalletTransactionType walletType,
        @Param("from") Instant from,
        @Param("to") Instant to
    );

    @Query("SELECT COALESCE(SUM(wt.amount), 0) FROM WalletTransaction wt " + "WHERE wt.walletType IN :types AND wt.transStatus = :status")
    double sumAmountByWalletTypesAndStatus(@Param("types") List<WalletTransactionType> types, @Param("status") TransactionStatus status);
}
