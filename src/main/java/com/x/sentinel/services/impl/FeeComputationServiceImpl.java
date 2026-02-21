package com.x.sentinel.services.impl;

import com.x.sentinel.dto.FeeComputationDto;
import com.x.sentinel.entities.FeeComputation;
import com.x.sentinel.repositories.FeeComputationRepository;
import com.x.sentinel.services.FeeComputationService;
import com.x.sentinel.services.FinalFeeEngine;
import com.x.sentinel.utils.SimulatedUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FeeComputationServiceImpl implements FeeComputationService {

    private final FinalFeeEngine engine = new FinalFeeEngine();
    private final FeeComputationRepository repo;

    @Override
    public FeeComputationDto compute(BigDecimal input) {
        BigDecimal fee = engine.computeFee(input);
        FeeComputation computation = FeeComputation.builder()
                .inputAmount(input)
                .feeAmount(fee)
                .algorithm("simple")
                .computedBy(SimulatedUserContext.getCurrentUser().username())
                .build();
        computation = repo.save(computation);
        return new FeeComputationDto(input, fee, "simple");
    }
}