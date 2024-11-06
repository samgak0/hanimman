package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "together_images")
public class TogetherImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String originalName;
    private String serverName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User userId;

    private int fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent")
    private Together parent;

    private Timestamp createAt;
    private Timestamp deletedAt;
}
