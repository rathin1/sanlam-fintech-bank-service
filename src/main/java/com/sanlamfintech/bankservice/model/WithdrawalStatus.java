package com.sanlamfintech.bankservice.model;

public enum WithdrawalStatus {

    SUCCESSFUL("Withdrawal successful"),
    INSUFFICIENT_FUNDS("Insufficient funds for withdrawal"),
    ACCOUNT_NOT_FOUND("Account not found"),
    INVALID_AMOUNT("Invalid amount");

    public final String description;

    WithdrawalStatus(String description) {
        this.description = description;
    }
}
