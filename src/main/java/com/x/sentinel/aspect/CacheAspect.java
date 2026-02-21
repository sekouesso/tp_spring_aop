package com.x.sentinel.aspect;

import com.x.sentinel.annotation.CustomCache;
import com.x.sentinel.entities.CacheEntry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@ConditionalOnProperty(name = "sentinel.cache.enabled", havingValue = "true")
public class CacheAspect {

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public CacheAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(customCache)")
    public Object cache(ProceedingJoinPoint jp, CustomCache customCache) throws Throwable {
        String key = customCache.key();
        if (key.isEmpty()) {
            key = jp.getSignature().toShortString() + ":" + Arrays.toString(jp.getArgs());
        }

        String fullKey = customCache.cacheName() + "::" + key;
        CacheEntry entry = cache.get(fullKey);

        // ===================== CACHE HIT =====================
        if (entry != null &&
                (customCache.ttlMs() == 0 ||
                        entry.getExpiresAt() == null ||
                        Instant.now().isBefore(entry.getExpiresAt()))) {

            entry.setHitCount(entry.getHitCount() + 1);
            entry.setLastAccessAt(Instant.now());
            System.out.println("CACHE HIT "+entry);
            if (entry.isCachedNull()) {
                return null;
            }

            JavaType type = objectMapper.getTypeFactory()
                    .constructFromCanonical(entry.getValueType());

            return objectMapper.readValue(entry.getValueJson(), type);
        }

        // ===================== CACHE MISS =====================
        Object result = jp.proceed();

        if (result != null || customCache.cacheNull()) {

            CacheEntry newEntry = CacheEntry.builder()
                    .cacheName(customCache.cacheName())
                    .key(key)
                    .valueType(result != null
                            ? objectMapper.getTypeFactory()
                            .constructType(result.getClass())
                            .toCanonical()
                            : null)
                    .valueJson(result != null
                            ? objectMapper.writeValueAsString(result)
                            : null)
                    .createdAt(Instant.now())
                    .lastAccessAt(Instant.now())
                    .expiresAt(customCache.ttlMs() > 0
                            ? Instant.now().plusMillis(customCache.ttlMs())
                            : null)
                    .cachedNull(result == null)
                    .hitCount(0)
                    .build();

            cache.put(fullKey, newEntry);
        }
        System.out.println("CACHE MISS "+cache);
        System.out.println("result "+result);
        return result;
    }
}
