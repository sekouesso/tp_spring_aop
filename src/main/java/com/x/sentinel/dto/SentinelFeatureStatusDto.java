package com.x.sentinel.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record SentinelFeatureStatusDto(
        boolean enabled,
        boolean auditEnabled,
        boolean cacheEnabled,
        boolean retryEnabled,
        boolean profilingEnabled,
        String activeProfile,
        Instant timestamp
) {}
