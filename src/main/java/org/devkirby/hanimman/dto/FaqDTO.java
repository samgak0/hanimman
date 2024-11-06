package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.*;
import org.devkirby.hanimman.entity.Faq;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant faqDeletedAt;
    private Instant faqCreateDate;
    private Instant faqModification;
    private Integer userId;

}
