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
    private Address primaryAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address secondlyAddress;

    @Column
    private String deviceUnique;

    @Column
    private Instant modifiedAt;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant deletedAt;

    @Column
    @Builder.Default
    private Integer brix = 15;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();  // 엔티티가 저장될 때 현재 시간으로 설정
        }
    }
}
