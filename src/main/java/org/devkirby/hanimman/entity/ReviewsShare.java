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
@Table(name="reviews_share")
public class ReviewsShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String content;

    @Column
    private Timestamp createAt;

    @Column
    private Timestamp deletedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id")
    private User memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id")
    private Share parentId;

}