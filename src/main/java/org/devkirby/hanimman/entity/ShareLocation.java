package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_locations")
public class ShareLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String latitude;

    @Column(nullable = false, length = 50)
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Address address;

    @Column(nullable = false, length = 50)
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Share share;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(columnDefinition = "TIMESTAMP(6)")
    private Instant modifiedAt;
}
