package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.SystemWalletTestSamples.*;
import static strip.domain.WalletTransactionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class SystemWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemWallet.class);
        SystemWallet systemWallet1 = getSystemWalletSample1();
        SystemWallet systemWallet2 = new SystemWallet();
        assertThat(systemWallet1).isNotEqualTo(systemWallet2);

        systemWallet2.setId(systemWallet1.getId());
        assertThat(systemWallet1).isEqualTo(systemWallet2);

        systemWallet2 = getSystemWalletSample2();
        assertThat(systemWallet1).isNotEqualTo(systemWallet2);
    }

    @Test
    void walletTransactionTest() {
        SystemWallet systemWallet = getSystemWalletRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        systemWallet.addWalletTransaction(walletTransactionBack);
        assertThat(systemWallet.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemWallet()).isEqualTo(systemWallet);

        systemWallet.removeWalletTransaction(walletTransactionBack);
        assertThat(systemWallet.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemWallet()).isNull();

        systemWallet.walletTransactions(new HashSet<>(Set.of(walletTransactionBack)));
        assertThat(systemWallet.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemWallet()).isEqualTo(systemWallet);

        systemWallet.setWalletTransactions(new HashSet<>());
        assertThat(systemWallet.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getSystemWallet()).isNull();
    }
}
