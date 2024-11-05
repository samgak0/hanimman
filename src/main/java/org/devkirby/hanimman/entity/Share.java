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
@Table(name="share")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String content;
    private int views;
    private Timestamp createAt;
    private Timestamp modifiedAt;
    private Timestamp deletedAt;
    private String location;
    private Timestamp locationDate;
    private String item;
    private int quantity;
    private boolean status;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name= "id")
//    private User user;
}
