package com.x.sentinel.controllers;

import com.x.sentinel.dto.SentinelFeatureStatusDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;

@RestController
@RequestMapping("/sentinel")
public class SentinelStatusController {

    private final Environment env;

    // Injections des propriétés avec des valeurs par défaut (false) si absentes
    @Value("${sentinel.enabled:false}") private boolean enabled;
    @Value("${sentinel.audit.enabled:false}") private boolean auditEnabled;
    @Value("${sentinel.cache.enabled:false}") private boolean cacheEnabled;
    @Value("${sentinel.retry.enabled:false}") private boolean retryEnabled;
    @Value("${sentinel.profiling.enabled:false}") private boolean profilingEnabled;

    public SentinelStatusController(Environment env) {
        this.env = env;
    }

    @GetMapping("/status")
    public SentinelFeatureStatusDto getStatus() {
        return SentinelFeatureStatusDto.builder()
                .enabled(enabled)
                .auditEnabled(auditEnabled)
                .cacheEnabled(cacheEnabled)
                .retryEnabled(retryEnabled)
                .profilingEnabled(profilingEnabled)
                .activeProfile(Arrays.toString(env.getActiveProfiles()))
                .timestamp(Instant.now())
                .build();
    }
}
