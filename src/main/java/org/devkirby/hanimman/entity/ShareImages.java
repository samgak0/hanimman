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
@Table(name = "share_images")
public class ShareImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String originalName;
    private String serverName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id")
    private User user;

    private int fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Share share;

    private Timestamp deletedAt;
    private Timestamp createAt;
}
