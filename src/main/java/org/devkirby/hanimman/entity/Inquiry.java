package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "inquiries")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Integer views = 0;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT NULL")
    private Instant modifiedAt;

    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT NULL")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}