package com.x.sentinel.services;

import com.x.sentinel.dto.FeeComputationDto;

import java.math.BigDecimal;

public interface FeeComputationService {
    FeeComputationDto compute(BigDecimal input);
}
