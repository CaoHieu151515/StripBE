package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.UserWalletTestSamples.*;
import static strip.domain.WalletDepositTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class WalletDepositTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletDeposit.class);
        WalletDeposit walletDeposit1 = getWalletDepositSample1();
        WalletDeposit walletDeposit2 = new WalletDeposit();
        assertThat(walletDeposit1).isNotEqualTo(walletDeposit2);

        walletDeposit2.setId(walletDeposit1.getId());
        assertThat(walletDeposit1).isEqualTo(walletDeposit2);

        walletDeposit2 = getWalletDepositSample2();
        assertThat(walletDeposit1).isNotEqualTo(walletDeposit2);
    }

    @Test
    void userWalletTest() {
        WalletDeposit walletDeposit = getWalletDepositRandomSampleGenerator();
        UserWallet userWalletBack = getUserWalletRandomSampleGenerator();

        walletDeposit.setUserWallet(userWalletBack);
        assertThat(walletDeposit.getUserWallet()).isEqualTo(userWalletBack);

        walletDeposit.userWallet(null);
        assertThat(walletDeposit.getUserWallet()).isNull();
    }
}
