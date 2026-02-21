package com.x.sentinel.dto;

// dto/TransferCreateDto.java
import com.x.sentinel.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferCreateDto(
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        Currency currency,
        String label
) {}