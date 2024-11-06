package org.devkirby.hanimman.entity;

import jakarta.annotation.Nullable;
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
@Table(name = "notice_files")
public class NoticeFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String originalName;
    private String serverName;

    @Column(length = 20)
    private String mineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User userId;

    private int fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent")
    private Notices parent;

    private Timestamp createAt;
    private Timestamp deletedAt;
}
