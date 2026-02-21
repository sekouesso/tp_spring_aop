package com.x.sentinel.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "profiling_spans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilingSpan {

    @Id
    private String spanId = UUID.randomUUID().toString();

    @Column(name = "parent_span_id")
    private String parentSpanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trace_id", nullable = false)
    private ProfilingTrace trace;

    @Column(nullable = false)
    private String operation;

    private int depth;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "duration_ms")
    private long durationMs;

    @Column(name = "order_index")
    private int orderIndex;

    @PrePersist
    protected void prePersist() {
        if (startedAt == null) {
            startedAt = Instant.now();
        }
    }
}
