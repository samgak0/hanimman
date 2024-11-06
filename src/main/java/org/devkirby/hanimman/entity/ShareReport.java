package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_reports")
public class ShareReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reported;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReportCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Share parent;
}
