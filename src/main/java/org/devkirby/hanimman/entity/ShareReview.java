package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_reviews")
public class ShareReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Share parent;
}
