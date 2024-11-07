package org.devkirby.hanimman.entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(length = 20, nullable = false)
    private String phonenum;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(length = 6, nullable = false)
    private String codenum;

    @Column(nullable = false)
    @Builder.Default
    private boolean privilege = false;

    @Column
    private Instant blockedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_region_id")
    private Region primaryRegion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondly_region_id")
    private Region secondlyRegion;

    @Column(nullable = false)
    private String deviceUniqueNum;

    @Column
    private Instant modifiedAt;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant deletedAt;
}
