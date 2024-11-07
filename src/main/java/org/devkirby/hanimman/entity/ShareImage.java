package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_images")
public class ShareImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String serverName;

    @Column(nullable = false)
    private String mineType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(nullable = false)
    private Integer fileSize;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Share parent;

    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT NULL")
    private Instant deletedAt;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Builder.Default
    private Instant createdAt = Instant.now();
}