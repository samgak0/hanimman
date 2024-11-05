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
    private int id;

    private String title;
    private String content;
    private int views;
    private Timestamp createAt;
    private Timestamp modifiedAt;
    private Timestamp deletedAt;
    private String meetingLocation;
    private Timestamp meetingAt;
    private String item;
    private int quantity;
    private boolean isEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id")
    private User user;
}
