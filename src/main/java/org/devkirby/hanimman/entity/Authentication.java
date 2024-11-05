package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@ToString
@Table(name="authentication")
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String value;

    @Column
    private Timestamp createAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id")
    private User parent;
}
