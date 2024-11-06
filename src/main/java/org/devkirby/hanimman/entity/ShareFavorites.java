package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "share_favorites")
public class ShareFavorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent", nullable = false)
    private Share parent;

    @Column(nullable = false)
    private Timestamp createAt;

    @PrePersist
    protected void onCreate(){
        this.createAt = new Timestamp(System.currentTimeMillis());
    }
}
