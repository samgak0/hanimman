package org.devkirby.hanimman.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant deletedAt;
    private String addressId;   // Region의 ID
    private String meetingLocation;
    private Instant meetingAt;
    private String item;
    private Integer quantity;
    private Boolean isEnd;
    private Integer userId;    // User의 ID

    // 이미지 리스트 추가
    private List<String> imageUrls;

    private List<Integer> imageIds;

    // 찜 여부
    @Builder.Default
    private boolean favorite = false;

    // 찜 수
    @Builder.Default
    private Integer favoriteCount = 0;

    private List<MultipartFile> files;

    private String address;
}
