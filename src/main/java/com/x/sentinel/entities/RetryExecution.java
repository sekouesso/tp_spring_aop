package com.x.sentinel.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// entity/RetryExecution.java
@Entity
@Table(name = "retry_executions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryExecution {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String operation;

    @Column(name = "max_attempts")
    private int maxAttempts;

    @Column(name = "delay_ms")
    private long delayMs;

    @Column(name = "retry_on")
    private String retryOn;   // CSV d'exceptions

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    private boolean success;

    @Column(name = "final_exception_class")
    private String finalExceptionClass;

    @Column(name = "final_exception_message", columnDefinition = "TEXT")
    private String finalExceptionMessage;

    @Column(name = "trace_id")
    private String traceId;

    @OneToMany(mappedBy = "retryExecution", cascade = CascadeType.ALL)
    private List<RetryAttempt> attempts = new ArrayList<>();
}
