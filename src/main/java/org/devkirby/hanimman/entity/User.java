package org.devkirby.hanimman.entity;

import java.time.Instant;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private Date birth;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Column(length = 20, nullable = false)
    private String phonenum;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(length = 20, nullable = false)
    private String codenum;

    @Column(nullable = false)
    private boolean privilege = false;

    @Column(nullable = false)
    private boolean blacklist = false;

    @Column(length = 255, nullable = false)
    private String neighborhood;

    @Column(length = 255, nullable = false)
    private String deviceUniqueNum;

    @Column
    private Instant modifiedAt;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant deletedAt;
}
