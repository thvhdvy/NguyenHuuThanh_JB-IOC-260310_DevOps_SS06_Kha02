package com.banking.banking_app.account;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final SecureRandom random = new SecureRandom();

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public AccountResponse create(CreateAccountRequest req) {
        BigDecimal balance = req.initialBalance() == null ? BigDecimal.ZERO : req.initialBalance();
        Account account = new Account(generateAccountNumber(), req.ownerName().trim(), balance);
        return AccountResponse.from(repository.save(account));
    }

    public AccountResponse getById(Long id) {
        return repository.findById(id).map(AccountResponse::from).orElseThrow(this::notFound);
    }

    public AccountResponse getByAccountNumber(String number) {
        return repository.findByAccountNumber(number).map(AccountResponse::from).orElseThrow(this::notFound);
    }

    public List<AccountResponse> list() {
        return repository.findAll().stream().map(AccountResponse::from).toList();
    }

    private String generateAccountNumber() {
        String number;
        do {
            number = String.format("%010d", random.nextLong(10_000_000_000L));
        } while (repository.existsByAccountNumber(number));
        return number;
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
    }
}
