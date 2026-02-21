package com.x.sentinel.entities;

// entity/ProfilingTrace.java
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profiling_traces")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilingTrace {

    @Id
    private String traceId;           // souvent UUID.toString() ou génération externe

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "root_operation", nullable = false)
    private String rootOperation;

    @Column(name = "total_duration_ms")
    private long totalDurationMs;

    @Column(name = "thread_name")
    private String threadName;

    @OneToMany(mappedBy = "trace", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<ProfilingSpan> spans = new ArrayList<>();

    @PrePersist
    protected void prePersist() {
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        if (startedAt == null) {
            startedAt = Instant.now();
        }
    }
}
