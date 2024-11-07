package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "together_participants")
public class TogetherParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Integer quantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean rejected = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Together parent;

    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT NULL")
    private Instant deletedAt;
}