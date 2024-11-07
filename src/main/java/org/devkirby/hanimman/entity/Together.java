package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "togethers")
public class Together {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
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
    private Region region;

    @Column(length = 255)
    private String meetingLocation;

    @Column(nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant meetingAt;

    @Column(length = 255)
    private String item;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isEnd = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}