package com.x.sentinel.services.impl;

import com.x.sentinel.annotation.CustomCache;
import com.x.sentinel.services.LongComputationService;
import org.springframework.stereotype.Service;

@Service
public class LongComputationServiceImpl implements LongComputationService {
    @CustomCache(cacheName = "longCalc", ttlMs = 60000)
    @Override
    public Object calculLong() {
        return "result";
    }
}
