package com.x.sentinel.services.impl;

import com.x.sentinel.annotation.CustomCache;
import com.x.sentinel.dto.AccountDto;
import com.x.sentinel.entities.Account;
import com.x.sentinel.repositories.AccountRepository;
import com.x.sentinel.services.AccountService;
import com.x.sentinel.services.LongComputationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final LongComputationService longComputationService;
    @Override
    public AccountDto createAccount(AccountDto dto) {
        Account account = Account.builder()
                .accountNumber(dto.accountNumber())
                .ownerName(dto.ownerName())
                .currency(dto.currency())
                .balance(dto.balance())
                .status(dto.status())
                .dailyTransferLimit(dto.dailyTransferLimit())
                .build();
        account = repository.save(account);
        return mapToDto(account);
    }

    // Autres méthodes : getBalance, debit, credit (avec @CustomCache sur getBalance par ex)
    @Override
    @CustomCache(cacheName = "balances", ttlMs = 60000)
    public BigDecimal getBalance(UUID id) {
        return repository.findById(id).orElseThrow().getBalance();
    }


    // Méthode pour démontrer self-invocation
    @Override
    public Object getDonnees() {
        return longComputationService.calculLong(); // Appel interne -> cache ne s'applique pas sans fix
    }

    @CustomCache(cacheName = "longCalc", ttlMs = 60000)
    public Object calculLong() {
        return "result";
    }


    private AccountDto mapToDto(Account account) {
        return new AccountDto(account.getId(), account.getAccountNumber(), account.getOwnerName(), account.getCurrency(), account.getBalance(), account.getStatus(), account.getDailyTransferLimit(), account.getCreatedAt(), account.getUpdatedAt());
    }
}






