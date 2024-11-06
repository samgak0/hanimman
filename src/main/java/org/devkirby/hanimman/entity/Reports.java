package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "reports")
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_id")
    private User reporterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reported_id")
    private User reportedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category")
    private ReportCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reported_post_id")
    private Share reportedPostId;
}
