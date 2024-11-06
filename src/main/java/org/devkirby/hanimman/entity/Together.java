package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="together")
public class Together {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "Integer DEFAULT 0")
    private Integer views;

    @Column(nullable = false)
    private Timestamp createAt;


    private Timestamp modifiedAt;
    private Timestamp deletedAt;

    @Column(nullable = false)
    private String meetingLocation;

    @Column(nullable = false)
    private Timestamp meetingAt;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean isEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User userId;

    @PrePersist
    protected void onCreate(){
        this.createAt = new Timestamp(System.currentTimeMillis());
    }
}
