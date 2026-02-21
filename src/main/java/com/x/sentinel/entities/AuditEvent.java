package com.x.sentinel.entities;

import com.x.sentinel.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuditEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String username;

    @Column(name = "method_signature", nullable = false)
    private String methodSignature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditStatus status;

    @Column(name = "duration_ms")
    private long durationMs;

    @Column(name = "args_logged")
    private boolean argsLogged;

    @Column(name = "args_masked")
    private boolean argsMasked;

    @Column(name = "args_summary", columnDefinition = "TEXT")
    private String argsSummary;

    @Column(name = "result_summary", columnDefinition = "TEXT")
    private String resultSummary;

    @Column(name = "exception_class")
    private String exceptionClass;

    @Column(name = "exception_message", columnDefinition = "TEXT")
    private String exceptionMessage;

    @Column(name = "trace_id")
    private String traceId;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}

