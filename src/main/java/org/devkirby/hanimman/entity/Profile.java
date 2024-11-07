package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String realName;

    @Column(nullable = false)
    private String serverName;

    @Column(nullable = false)
    private String mineType;

    @Column(nullable = false)
    private Integer fileSize;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User parent;
}
