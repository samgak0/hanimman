package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_participants")
public class ShareParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean rejected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent", nullable = false)
    private Share parent;

    @PrePersist
    protected void onCreate(){
        this.date = new Timestamp(System.currentTimeMillis());
    }
}
