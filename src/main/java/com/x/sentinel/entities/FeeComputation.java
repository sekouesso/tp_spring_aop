package com.x.sentinel.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// entity/FeeComputation.java
@Entity
@Table(name = "fee_computations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeComputation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "input_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal inputAmount;

    @Column(name = "fee_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal feeAmount;

    private String algorithm;

    @Column(name = "computed_at", nullable = false)
    private LocalDateTime computedAt;

    @Column(name = "computed_by")
    private String computedBy;

    @PrePersist
    protected void onCreate() { computedAt = LocalDateTime.now(); }
}
