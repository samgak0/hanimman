package org.devkirby.hanimman.entity;

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
@Table(name = "markets")
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MarketCategory category;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String latitude;

    @Column(length = 50, nullable = false)
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @Column(length = 255, nullable = false)
    private String address;
}