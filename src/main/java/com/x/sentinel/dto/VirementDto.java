package com.x.sentinel.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record VirementDto(
        UUID fromAccount, UUID toAccount, BigDecimal amount
) {
}
