package com.x.sentinel.services;

import com.x.sentinel.annotation.CustomCache;

public interface LongComputationService {
    @CustomCache(cacheName = "longCalc", ttlMs = 60000)
    Object calculLong();
}
