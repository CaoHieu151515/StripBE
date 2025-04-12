package com.example.strip.Models.Response;

import com.example.strip.Models.Transaction;
import com.example.strip.Models.UserWallet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletResponse {
    @SerializedName("userWallet")
    private UserWallet userWallet;

    @SerializedName("transactions")
    private List<Transaction> transactions;

    public UserWallet getUserWallet() { return userWallet; }
    public List<Transaction> getTransactions() { return transactions; }
}
