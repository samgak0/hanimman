package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_participants")
public class ShareParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false)
    private Integer quantity;

    private Instant rejected;
    private Instant accepted;
    private Instant complete;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Share parent;

    @Column(columnDefinition = "TIMESTAMP(6)")
    private Instant deletedAt;
}
