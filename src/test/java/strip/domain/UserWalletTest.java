package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.UserWalletTestSamples.*;
import static strip.domain.WalletDepositTestSamples.*;
import static strip.domain.WalletTransactionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class UserWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserWallet.class);
        UserWallet userWallet1 = getUserWalletSample1();
        UserWallet userWallet2 = new UserWallet();
        assertThat(userWallet1).isNotEqualTo(userWallet2);

        userWallet2.setId(userWallet1.getId());
        assertThat(userWallet1).isEqualTo(userWallet2);

        userWallet2 = getUserWalletSample2();
        assertThat(userWallet1).isNotEqualTo(userWallet2);
    }

    @Test
    void walletTransactionTest() {
        UserWallet userWallet = getUserWalletRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        userWallet.addWalletTransaction(walletTransactionBack);
        assertThat(userWallet.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getUserWallet()).isEqualTo(userWallet);

        userWallet.removeWalletTransaction(walletTransactionBack);
        assertThat(userWallet.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getUserWallet()).isNull();

        userWallet.walletTransactions(new HashSet<>(Set.of(walletTransactionBack)));
        assertThat(userWallet.getWalletTransactions()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getUserWallet()).isEqualTo(userWallet);

        userWallet.setWalletTransactions(new HashSet<>());
        assertThat(userWallet.getWalletTransactions()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getUserWallet()).isNull();
    }
}
