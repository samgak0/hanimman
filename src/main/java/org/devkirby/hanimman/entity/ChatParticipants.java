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
@Table(name="chat_participants")
public class ChatParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Timestamp joinedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "room_id")
    private User roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User userId;



}
