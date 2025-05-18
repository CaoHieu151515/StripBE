package strip.service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.ExchangeRate;
import strip.domain.User;
import strip.domain.UserWallet;
import strip.domain.WalletTransaction;
import strip.domain.WithdrawRequest;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.domain.enumeration.WithdrawStatus;
import strip.repository.ExchangeRateRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.WalletTransactionRepository;
import strip.repository.WithdrawRequestRepository;
import strip.security.SecurityUtils;
import strip.service.dto.WithdrawRequestPaypalDTO;
import strip.web.rest.errors.BadRequestAlertException;

@Service
@Transactional
public class WithdrawService {

    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;
    private final WithdrawRequestRepository withdrawRequestRepository;
    private final PaypalPayoutService paypalPayoutService;
    private final ExchangeRateRepository exchangeRateRepository;

    public WithdrawService(
        UserRepository userRepository,
        UserWalletRepository userWalletRepository,
        WalletTransactionRepository walletTransactionRepository,
        WithdrawRequestRepository withdrawRequestRepository,
        PaypalPayoutService paypalPayoutService,
        ExchangeRateRepository exchangeRateRepository
    ) {
        this.userRepository = userRepository;
        this.userWalletRepository = userWalletRepository;
        this.withdrawRequestRepository = withdrawRequestRepository;
        this.paypalPayoutService = paypalPayoutService;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Transactional
    public void requestWithdraw(WithdrawRequestPaypalDTO dto) {
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "withdraw", "nouser"));

        // 2. Lấy ví
        UserWallet wallet = userWalletRepository
            .findByUser(user)
            .orElseThrow(() -> new BadRequestAlertException("Wallet not found", "withdraw", "nowallet"));

        double vndAmount = dto.getAmount();
        if (wallet.getCurrent() < vndAmount) {
            throw new BadRequestAlertException("Insufficient balance", "withdraw", "lowbalance");
        }

        // 3. Lấy tỷ giá mới nhất
        ExchangeRate rate = exchangeRateRepository
            .findTopByFromCurrencyAndToCurrencyOrderByDateDesc("USD", "VND")
            .orElseThrow(() -> new BadRequestAlertException("Tỷ giá chưa được cập nhật", "withdraw", "norate"));

        double exchangeRate = rate.getRate();
        double usdAmount = Math.round((vndAmount / exchangeRate) * 100.0) / 100.0; // làm tròn 2 số lẻ

        // 4. Ghi yêu cầu rút tiền
        UUID requestUuid = UUID.randomUUID();
        String note = "Rút tiền từ S-Trip";
        WithdrawRequest request = new WithdrawRequest();
        request.setUser(user);
        request.setUuid(requestUuid);
        request.setAmount(vndAmount); // VND
        request.setCurrency("VND");
        request.setEmail(dto.getEmail());
        request.setNote(note);
        request.setRequestDate(Instant.now());
        request.setStatus(WithdrawStatus.PENDING);
        request.setExchangeRate(exchangeRate);
        request.setConvertedAmountUsd(usdAmount);
        withdrawRequestRepository.save(request);

        // 5. Gửi payout PayPal (bằng USD)
        String payoutId;
        try {
            payoutId = paypalPayoutService.payout(dto.getEmail(), usdAmount, "USD", note);
        } catch (Exception e) {
            request.setStatus(WithdrawStatus.FAILED);
            request.setProcessedDate(Instant.now());
            withdrawRequestRepository.save(request);
            throw new RuntimeException("PayPal payout failed: " + e.getMessage());
        }

        // 6. Ghi transaction & cập nhật ví
        WalletTransaction tx = new WalletTransaction();
        tx.setTransID(UUID.randomUUID());
        tx.setAmount(vndAmount);
        tx.setDate(Instant.now());
        tx.setWalletType(WalletTransactionType.WITHDRAW);
        tx.setTransStatus(TransactionStatus.SUCCESS);
        tx.setTransactionThirdPartyID(requestUuid.toString());
        tx.setUserWallet(wallet);
        wallet.addWalletTransactionAndUpdateBalance(tx);

        // 7. Cập nhật lại yêu cầu với PayPal payout ID & trạng thái
        request.setPaypalId(payoutId);
        request.setStatus(WithdrawStatus.SUCCESS);
        request.setProcessedDate(Instant.now());
        withdrawRequestRepository.save(request);
    }

    public void testCreateWithdrawRequest() {
        // Lấy user hiện tại từ context
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "withdraw", "nouser"));

        // Tạo bản ghi WithdrawRequest mẫu
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setUuid(UUID.randomUUID());
        withdrawRequest.setUser(user);
        withdrawRequest.setAmount(5.0);
        withdrawRequest.setCurrency("USD");
        withdrawRequest.setEmail("sandbox-paypal@example.com");
        withdrawRequest.setNote("Test lưu bản ghi rút tiền");
        withdrawRequest.setRequestDate(Instant.now());
        withdrawRequest.setStatus(WithdrawStatus.PENDING);

        // Lưu xuống DB
        withdrawRequestRepository.save(withdrawRequest);
    }
}
