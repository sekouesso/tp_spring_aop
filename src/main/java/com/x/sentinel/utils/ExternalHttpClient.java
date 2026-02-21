package com.x.sentinel.utils;

import com.x.sentinel.annotation.RetryOnFailure;

public class ExternalHttpClient {

    @RetryOnFailure(maxAttempts = 3, delayMs = 300)
    public String call() {

        if (Math.random() > 0.5) {
            throw new RuntimeException("Remote service down");
        }

        return "OK";
    }
}

