package org.devkirby.hanimman.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
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
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime deletedAt;
}
