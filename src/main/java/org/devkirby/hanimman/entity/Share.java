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
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(nullable = false)
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
    private String location;

    @Column
    private Instant locationDate;

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