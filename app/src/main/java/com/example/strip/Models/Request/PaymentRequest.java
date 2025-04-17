package com.example.strip.Models.Request;


public class PaymentRequest {
    private String nonce;
    private String amount;

    public PaymentRequest(String amount, String nonce) {
        this.amount = amount;
        this.nonce = nonce;
    }

    public String getNonce() {
        return nonce;
    }

    public String getAmount() {
        return amount;
    }
}