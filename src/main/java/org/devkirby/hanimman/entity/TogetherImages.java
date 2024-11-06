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
@Table(name = "together_images")
public class TogetherImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String serverName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable = false)
    private User userId;

    @Column(nullable = false)
    private Integer fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent", nullable = false)
    private Together parent;

    @Column(nullable = false)
    private Timestamp createAt;

    private Timestamp deletedAt;

    @PrePersist
    protected void onCreate(){
        this.createAt = new Timestamp(System.currentTimeMillis());
    }
}
