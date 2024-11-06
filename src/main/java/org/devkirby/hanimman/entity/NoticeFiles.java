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

    @Column(length = 100, nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String serverName;

    @Column(length = 20, nullable = false)
    private String mineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User userId;

    @Column(nullable = false)
    private int fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent", nullable = false)
    private Notices parent;

    @Column(nullable = false)
    private Timestamp createAt;

    private Timestamp deletedAt;

    @PrePersist
    protected void onCreate(){
        this.createAt = new Timestamp(System.currentTimeMillis());
    }
}
