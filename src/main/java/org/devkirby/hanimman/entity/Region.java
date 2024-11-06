package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name="region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id", insertable=false, updatable=false)
    private City cityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id", insertable=false, updatable=false)
    private Country countryId;

}
