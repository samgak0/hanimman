package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;

    @Column
    private Date birth;

//    @Enumerated(EnumType.STRING)
//   @Column
//    private Gender gender;

    @Column(length = 20)
    private String phonenum;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20)
    private String codenum;

    @Column
    private boolean privilege;

    @Column
    private boolean blacklist;

    @Column(length = 255)
    private String neighborhood;

    @Column(length = 255)
    private String deviceUniqueNum;

    @Column(nullable = true)
    private Timestamp modifiedAt;

    @Column
    private Timestamp createdAt;

    @Column(nullable = true)
    private Timestamp deletedAt;
}
