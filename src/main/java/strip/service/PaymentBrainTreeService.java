package strip.service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import strip.domain.Payment;
import strip.domain.SystemWallet;
import strip.domain.User;
import strip.domain.UserWallet;
import strip.domain.WalletTransaction;
import strip.domain.enumeration.PaymentStatus;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.PaymentRepository;
import strip.repository.SystemWalletRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.security.SecurityUtils;

@Service
public class PaymentBrainTreeService {

    private final BraintreeGateway braintreeGateway;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final UserWalletRepository userWalletRepository;
    private final SystemWalletRepository systemWalletRepository;

    public PaymentBrainTreeService(
        BraintreeGateway braintreeGateway,
        UserRepository userRepository,
        PaymentRepository paymentRepository,
        UserWalletRepository userWalletRepository,
        SystemWalletRepository systemWalletRepository
    ) {
        this.braintreeGateway = braintreeGateway;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.userWalletRepository = userWalletRepository;
        this.systemWalletRepository = systemWalletRepository;
    }

    public String generateClientToken() {
        return braintreeGateway.clientToken().generate();
    }

    @Transactional
    public Result<Transaction> checkout(String nonce, BigDecimal amount) {
        System.out.println("🧾 Received nonce = " + nonce);
        System.out.println("💰 Amount = " + amount);

        TransactionRequest request = new TransactionRequest()
            .amount(amount)
            .paymentMethodNonce(nonce)
            .options()
            .submitForSettlement(true)
            .done();

        Result<Transaction> result = braintreeGateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();

            // ✅ Xử lý lưu Payment và cập nhật UserWallet
            savePaymentAndTopupWallet(transaction);

            return result;
        }

        return result;
    }

    public void savePaymentAndTopupWallet(Transaction transaction) {
        String transactionId = transaction.getId();
        BigDecimal amount = transaction.getAmount();
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // ✅ Lưu thông tin Payment
        Payment payment = new Payment();
        payment.setPaymentID(UUID.randomUUID());
        payment.setAmount(amount.doubleValue());
        payment.setPaymentDate(Instant.now());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setUser(currentUser);
        paymentRepository.save(payment);

        // ✅ Lấy hoặc tạo ví
        UserWallet wallet = userWalletRepository
            .findByUser(currentUser)
            .orElseGet(() -> {
                UserWallet w = new UserWallet();
                w.setUserWallet(UUID.randomUUID());
                w.setUser(currentUser);
                w.setBefore(0D);
                w.setCurrent(0D);
                w.setAmount(0D);
                return w;
            });

        // ✅ Tạo WalletTransaction
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setTransID(UUID.randomUUID());
        walletTransaction.setAmount(amount.doubleValue());
        walletTransaction.setDate(Instant.now());
        walletTransaction.setWalletType(WalletTransactionType.DEPOSIT);
        walletTransaction.setTransStatus(TransactionStatus.SUCCESS);
        walletTransaction.setTransactionThirdPartyID(transactionId);
        walletTransaction.setPayment(payment);
        walletTransaction.setUserWallet(wallet);
        wallet.getWalletTransactions().size();

        wallet.addWalletTransactionAndUpdateBalance(walletTransaction);

        userWalletRepository.save(wallet);
        // walletTransactionRepository.save(walletTransaction);

        SystemWallet sys = systemWalletRepository
            .findTopByOrderByMobifyDateDesc()
            .orElseThrow(() -> new EntityNotFoundException("not found"));
        WalletTransaction systrans = new WalletTransaction();
        systrans.setTransID(UUID.randomUUID());
        systrans.setAmount(amount.doubleValue());
        systrans.setDate(Instant.now());
        systrans.setWalletType(WalletTransactionType.SYSTEM_GAIN_DEPOSIT);
        systrans.setTransStatus(TransactionStatus.SUCCESS);
        systrans.setTransactionThirdPartyID(transactionId);
        systrans.setPayment(payment);
        systrans.setUserWallet(wallet);
        sys.addWalletTransactionAndUpdateBalance(systrans);
        systemWalletRepository.save(sys);
    }
}
