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
@Table(name = "faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer views;

    @Column(nullable = false)
    private Timestamp createAt;

    private Timestamp modifiedAt;
    private Timestamp deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User userId;

    @PrePersist
    protected void onCreate(){
        this.createAt = new Timestamp(System.currentTimeMillis());
    }
}
