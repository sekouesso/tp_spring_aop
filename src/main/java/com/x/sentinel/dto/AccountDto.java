package com.x.sentinel.dto;

// dto/AccountDto.java
import com.x.sentinel.enums.AccountStatus;
import com.x.sentinel.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountDto(
        UUID id,
        String accountNumber,
        String ownerName,
        Currency currency,
        BigDecimal balance,
        AccountStatus status,
        BigDecimal dailyTransferLimit,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
