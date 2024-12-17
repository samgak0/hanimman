package org.devkirby.hanimman.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShareTogetherDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String address;
    private LocalDateTime dateAt;
    private Integer quantity;
    private Integer price;
    private Integer userId;
    private String type;
    private Integer imageId;
    private Long favorite;
    private Long participant;
}
