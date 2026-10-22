package com.banking.banking_app.account;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(Long id, String accountNumber, String ownerName, BigDecimal balance, Instant createdAt) {

    static AccountResponse from(Account a) {
        return new AccountResponse(a.getId(), a.getAccountNumber(), a.getOwnerName(), a.getBalance(), a.getCreatedAt());
    }
}
