package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.SystemTempWalletTestSamples.*;
import static strip.domain.WalletTransactionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class SystemTempWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemTempWallet.class);
        SystemTempWallet systemTempWallet1 = getSystemTempWalletSample1();
        SystemTempWallet systemTempWallet2 = new SystemTempWallet();
        assertThat(systemTempWallet1).isNotEqualTo(systemTempWallet2);

        systemTempWallet2.setId(systemTempWallet1.getId());
        assertThat(systemTempWallet1).isEqualTo(systemTempWallet2);

        systemTempWallet2 = getSystemTempWalletSample2();
        assertThat(systemTempWallet1).isNotEqualTo(systemTempWallet2);
    }

    @Test
    void walletTransactionTest() {
        SystemTempWallet systemTempWallet = getSystemTempWalletRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        systemTempWallet.addWalletTransaction(walletTransactionBack);
        assertThat(systemTempWallet.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemTempWallet()).isEqualTo(systemTempWallet);

        systemTempWallet.removeWalletTransaction(walletTransactionBack);
        assertThat(systemTempWallet.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemTempWallet()).isNull();

        systemTempWallet.walletTransactions(new HashSet<>(Set.of(walletTransactionBack)));
        assertThat(systemTempWallet.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemTempWallet()).isEqualTo(systemTempWallet);

        systemTempWallet.setWalletTransactions(new HashSet<>());
        assertThat(systemTempWallet.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemTempWallet()).isNull();
    }
}
