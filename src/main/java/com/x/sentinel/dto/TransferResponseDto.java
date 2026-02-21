package com.x.sentinel.dto;

// dto/TransferResponseDto.java
import com.x.sentinel.enums.Currency;
import com.x.sentinel.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponseDto(
        UUID id,
        String reference,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        Currency currency,
        String label,
        TransferStatus status,
        LocalDateTime requestedAt,
        LocalDateTime executedAt,
        String failureCode,
        String failureMessage
) {}
