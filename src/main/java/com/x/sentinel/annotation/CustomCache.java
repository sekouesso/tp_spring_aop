package com.x.sentinel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SentinelCache {
    String cacheName();
    long ttlSeconds() default 300;
}

 */



@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCache {
    String cacheName();
    long ttlMs() default 0;
    String key() default "";
    boolean cacheNull() default false;
}


