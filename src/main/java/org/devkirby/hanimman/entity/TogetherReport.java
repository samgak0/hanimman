package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "together_reports")
public class TogetherReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User reported;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ReportCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Together parent;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Builder.Default
    private Instant createdAt = Instant.now();
}