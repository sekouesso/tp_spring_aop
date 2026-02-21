package com.x.sentinel.entities;

// entity/CacheEntry.java
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cache_entries",
        indexes = @Index(columnList = "cache_name, key", unique = true))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "cache_name", nullable = false)
    private String cacheName;

    @Column(nullable = false,name = "parameter_key")
    private String key;

    @Column(name = "value_type")
    private String valueType;       // ex: "com.example.dto.XXX"

    @Lob
    @Column(name = "value_json", columnDefinition = "TEXT")
    private String valueJson;       // JSON sérialisé (ou BLOB si binaire)

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "last_access_at")
    private Instant lastAccessAt;

    @Column(name = "hit_count")
    private long hitCount = 0;

    @Column(name = "cached_null")
    private boolean cachedNull = false;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        lastAccessAt = Instant.now();
    }
}
