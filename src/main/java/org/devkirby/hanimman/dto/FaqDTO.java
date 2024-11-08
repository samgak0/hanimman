package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant deletedAt;
    private Instant createAt;
    private Instant modifiedAt;
    private Integer userId;

}
