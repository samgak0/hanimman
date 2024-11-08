package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "markets")
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MarketCategory category;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String latitude;

    @Column(length = 50, nullable = false)
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Address address;

    @Column(nullable = false)
    private String addressDetail;
}