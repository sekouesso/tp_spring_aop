package com.x.sentinel.dto;

import java.math.BigDecimal;

public record FeeComputationDto(BigDecimal inputAmount, BigDecimal feeAmount, String algorithm) {}
