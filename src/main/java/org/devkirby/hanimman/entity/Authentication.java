package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "authentications")
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    @Builder.Default
    private Instant createAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}