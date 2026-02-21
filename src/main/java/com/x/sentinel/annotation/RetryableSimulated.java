package com.x.sentinel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryableSimulated {
    int maxAttempts() default 3;
    long delayMs() default 500;
    Class<? extends Throwable>[] retryOn() default {Exception.class};
}
