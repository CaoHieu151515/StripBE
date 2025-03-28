package strip.service.dto;

import java.util.List;
import strip.domain.UserWallet;
import strip.domain.WalletTransaction;

public class UserWalletWithTransactionsDTO {

    private UserWallet userWallet;
    private List<WalletTransaction> transactions;

    public UserWalletWithTransactionsDTO(UserWallet userWallet, List<WalletTransaction> transactions) {
        this.userWallet = userWallet;
        this.transactions = transactions;
    }

    public UserWallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public List<WalletTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<WalletTransaction> transactions) {
        this.transactions = transactions;
    }
}
