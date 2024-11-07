package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "inquiry_files")
public class InquiryFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String originalName;

    @Column(length = 100, nullable = false)
    private String serverName;

    @Column(length = 20, nullable = false)
    private String mineType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(nullable = false)
    private Integer fileSize;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Inquiry parent;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT NULL")
    private Instant deletedAt;
}