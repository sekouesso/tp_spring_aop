package com.x.sentinel.donnertest;

import com.x.sentinel.repositories.AccountRepository;
import com.x.sentinel.repositories.FeeComputationRepository;
import com.x.sentinel.services.FeeComputationService;
import com.x.sentinel.utils.SimulatedUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final FeeComputationRepository feeComputationRepository;
    private final FeeComputationService feeComputationService;


    @Override
    public void run(String... args) {
        SimulatedUserContext.setCurrentUser(
                new SimulatedUserContext.SimulatedUser("system", List.of("ADMIN"))
        );
        feeComputationService.compute(BigDecimal.valueOf(290.0));
        System.out.println("✅ Données initialisées");
    }
}
