package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.PackageDriverTestSamples.*;
import static strip.domain.PaymentTestSamples.*;
import static strip.domain.WalletTransactionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void packageDriverTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PackageDriver packageDriverBack = getPackageDriverRandomSampleGenerator();

        payment.setPackageDriver(packageDriverBack);
        assertThat(payment.getPackageDriver()).isEqualTo(packageDriverBack);

        payment.packageDriver(null);
        assertThat(payment.getPackageDriver()).isNull();
    }

    @Test
    void walletTransactionTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        payment.addWalletTransaction(walletTransactionBack);
        assertThat(payment.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getPayment()).isEqualTo(payment);

        payment.removeWalletTransaction(walletTransactionBack);
        assertThat(payment.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getPayment()).isNull();

        payment.walletTransactions(new HashSet<>(Set.of(walletTransactionBack)));
        assertThat(payment.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getPayment()).isEqualTo(payment);

        payment.setWalletTransactions(new HashSet<>());
        assertThat(payment.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getPayment()).isNull();
    }
}
