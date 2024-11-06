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

    @Column(length = 255, nullable = false)
    private String originalName;

    @Column(length = 255, nullable = false)
    private String serverName;

    @Column(length = 255, nullable = false)
    private String mineType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private Integer fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    private Share parent;

    @Column
    private Instant deletedAt;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}