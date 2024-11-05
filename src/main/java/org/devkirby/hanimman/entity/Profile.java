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
@Table(name="profile")
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

    @Column
    private Timestamp createAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id")
    private User parent;

}
