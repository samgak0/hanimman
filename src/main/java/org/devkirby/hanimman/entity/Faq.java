package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "faqs")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Integer views = 0;

    @Column
    private Instant faqDeletedAt;

    @Column(nullable = false)
    @Builder.Default
    private Instant faqCreateDate = Instant.now();

    @Column
    private Instant faqModification;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
