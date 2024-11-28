package org.devkirby.hanimman.dto;

import java.time.Instant;
import java.util.List;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 파일 리스트 추가
    private List<String> fileUrls;

    private List<Integer> imageIds;

    private List<MultipartFile> files;
}
