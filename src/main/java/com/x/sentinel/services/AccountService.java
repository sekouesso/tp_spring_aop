package com.x.sentinel.services;

import com.x.sentinel.dto.AccountDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {

    AccountDto createAccount(AccountDto dto);
    BigDecimal getBalance(UUID id);

    // Méthode pour démontrer self-invocation
    Object getDonnees();
}
