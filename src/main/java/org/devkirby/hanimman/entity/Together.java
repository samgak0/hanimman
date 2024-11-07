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

    @Column(length = 255, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Integer views = 0;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant modifiedAt;

    @Column
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @Column(length = 255)
    private String meetingLocation;

    @Column
    private Instant meetingAt;

    @Column(length = 255)
    private String item;

    @Column
    private Integer quantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isEnd = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}