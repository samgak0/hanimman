package org.devkirby.hanimman.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String realName;

    @Column(length = 255)
    private String serverName;

    @Column(length = 255)
    private String mineType;

    @Column
    private Integer fileSize;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", nullable = false)
    private User parent;

}
