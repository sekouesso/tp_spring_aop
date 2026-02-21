package com.x.sentinel.services;

import com.x.sentinel.annotation.RetryOnFailure;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public final class FinalFeeEngine {
    @RetryOnFailure(maxAttempts = 4, delayMs = 1000)
    public BigDecimal computeFee(BigDecimal amount) {
        if (Math.random() > 0.5) {
            System.out.println("[FinalFeeEngine] Échec du calcul, tentative de levée d'exception...");
            throw new RuntimeException("Echec simulé");
        }
        return amount.multiply(BigDecimal.valueOf(0.01)); // 1%
    }
}
