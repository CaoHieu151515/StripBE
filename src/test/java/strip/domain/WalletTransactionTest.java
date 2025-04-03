package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.PaymentTestSamples.*;
import static strip.domain.SystemWalletTestSamples.*;
import static strip.domain.UserWalletTestSamples.*;
import static strip.domain.WalletTransactionTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class WalletTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletTransaction.class);
        WalletTransaction walletTransaction1 = getWalletTransactionSample1();
        WalletTransaction walletTransaction2 = new WalletTransaction();
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);

        walletTransaction2.setId(walletTransaction1.getId());
        assertThat(walletTransaction1).isEqualTo(walletTransaction2);

        walletTransaction2 = getWalletTransactionSample2();
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);
    }

    @Test
    void systemWalletTest() {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        SystemWallet systemWalletBack = getSystemWalletRandomSampleGenerator();

        walletTransaction.setSystemWallet(systemWalletBack);
        assertThat(walletTransaction.getSystemWallet()).isEqualTo(systemWalletBack);

        walletTransaction.systemWallet(null);
        assertThat(walletTransaction.getSystemWallet()).isNull();
    }

    @Test
    void paymentTest() {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        walletTransaction.setPayment(paymentBack);
        assertThat(walletTransaction.getPayment()).isEqualTo(paymentBack);

        walletTransaction.payment(null);
        assertThat(walletTransaction.getPayment()).isNull();
    }

    @Test
    void userWalletTest() {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        UserWallet userWalletBack = getUserWalletRandomSampleGenerator();

        walletTransaction.setUserWallet(userWalletBack);
        assertThat(walletTransaction.getUserWallet()).isEqualTo(userWalletBack);

        walletTransaction.userWallet(null);
        assertThat(walletTransaction.getUserWallet()).isNull();
    }
}
