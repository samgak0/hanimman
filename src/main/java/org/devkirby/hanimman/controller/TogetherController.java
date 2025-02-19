package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.service.TogetherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;

    private static final String ERROR_TITLE_LENGTH = "제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이 : %d";
    private static final String ERROR_CONTENT_LENGTH = "내용의 길이는 65535자 이하여야 합니다. 현재 길이 : %d";
    private static final String ERROR_IMAGE_LIMIT = "이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수 : %d";
    private static final String ERROR_MEETING_TIME = "같이가요 시간은 현재 시간으로부터 한 시간 이후, 7일 이전이어야 합니다.";
    private static final String ERROR_QUANTITY = "수량은 1개 이상이어야 합니다.";
    private static final String ERROR_PRICE = "가격은 0원 이상이어야 합니다.";
    private static final String ERROR_ITEM_LENGTH = "물품의 길이는 1자 이상, 50자 이하여야 합니다. 현재 길이 : %d";
    private static final String ERROR_UNAUTHORIZED_EDIT = "본인이 작성한 게시글만 수정할 수 있습니다.";
    private static final String ERROR_UNAUTHORIZED_DELETE = "본인이 작성한 게시글만 삭제할 수 있습니다.";

    private static final String MSG_SUCCESS_CREATE = "같이가요 게시글 작성에 성공했습니다.";
    private static final String MSG_SUCCESS_UPDATE = "같이가요 게시글 수정에 성공했습니다.";
    private static final String MSG_SUCCESS_DELETE = "같이가요 게시글 삭제에 성공했습니다.";

    /**
     * 같이가요 게시글 작성
     * 
     * @param togetherDTO 같이가요 게시글 정보
     * @param files       첨부 파일
     * @param loginUser   로그인 유저
     * @return 작성 결과
     * @throws IOException 파일 처리 예외
     */
    @PostMapping("/create")
    public Map<String, Object> createTogether(@RequestPart("togetherDTO") TogetherDTO togetherDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Instant now = Instant.now();
        Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);
        Instant limitDay = now.plus(7, ChronoUnit.DAYS);
        String primaryAddressId = null;

        if (togetherDTO.getTitle().length() > 255 || togetherDTO.getTitle().isEmpty()) {
            throw new IllegalStateException(String.format(ERROR_TITLE_LENGTH, togetherDTO.getTitle().length()));
        } else if (togetherDTO.getContent().length() > 1000 || togetherDTO.getContent().isEmpty()) {
            throw new IllegalStateException(String.format(ERROR_CONTENT_LENGTH, togetherDTO.getContent().length()));
        } else if (files != null && files.size() > 10) {
            throw new IllegalStateException(String.format(ERROR_IMAGE_LIMIT, files.size()));
        } else if (togetherDTO.getMeetingAt().isBefore(oneHourLater) || togetherDTO.getMeetingAt().isAfter(limitDay)) {
            throw new IllegalStateException(ERROR_MEETING_TIME);
        } else if (togetherDTO.getQuantity() < 1) {
            throw new IllegalStateException(ERROR_QUANTITY);
        } else if (togetherDTO.getPrice() < 0) {
            throw new IllegalStateException(ERROR_PRICE);
        } else if (togetherDTO.getItem().isEmpty() || togetherDTO.getItem().length() > 50) {
            throw new IllegalStateException(String.format(ERROR_ITEM_LENGTH, togetherDTO.getItem().length()));
        } else {
            togetherDTO.setUserId(loginUser.getId());
            if (files != null && !files.isEmpty()) {
                togetherDTO.setFiles(files); // 파일 설정
            }
            Integer id = togetherService.create(togetherDTO, primaryAddressId);
            map.put("code", 200);
            map.put("id", id);
            map.put("msg", MSG_SUCCESS_CREATE);
            return map;
        }
    }

    /**
     * 같이가요 게시글 조회
     * 
     * @param id        게시글 ID
     * @param loginUser 로그인 유저
     * @return 게시글 정보
     */
    @GetMapping("/{id}")
    public TogetherDTO readTogether(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        return togetherService.read(id, loginUser);
    }

    /**
     * 같이가요 게시글 수정
     * 
     * @param id          게시글 ID
     * @param togetherDTO 같이가요 게시글 정보
     * @param files       첨부 파일
     * @param loginUser   로그인 유저
     * @return 수정 결과
     * @throws IOException 파일 처리 예외
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateTogether(@PathVariable Integer id,
            @RequestPart(name = "togetherDTO") TogetherDTO togetherDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        togetherDTO.setId(id);
        Instant now = Instant.now();
        Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);
        Instant limitDay = now.plus(7, ChronoUnit.DAYS);
        if (!loginUser.getId().equals(togetherDTO.getUserId())) {
            throw new IllegalArgumentException(ERROR_UNAUTHORIZED_EDIT);
        } else if (togetherDTO.getTitle().length() > 255 || togetherDTO.getTitle().isEmpty()) {
            throw new IllegalStateException(String.format(ERROR_TITLE_LENGTH, togetherDTO.getTitle().length()));
        } else if (togetherDTO.getContent().length() > 65535 || togetherDTO.getContent().isEmpty()) {
            throw new IllegalStateException(String.format(ERROR_CONTENT_LENGTH, togetherDTO.getContent().length()));
        } else if (togetherDTO.getMeetingAt().isBefore(oneHourLater) || togetherDTO.getMeetingAt().isAfter(limitDay)) {
            throw new IllegalStateException(ERROR_MEETING_TIME);
        } else if (togetherDTO.getQuantity() < 1) {
            throw new IllegalStateException(ERROR_QUANTITY);
        } else if (togetherDTO.getPrice() < 0) {
            throw new IllegalStateException(ERROR_PRICE);
        } else if (togetherDTO.getItem().isEmpty() || togetherDTO.getItem().length() > 50) {
            throw new IllegalStateException(String.format(ERROR_ITEM_LENGTH, togetherDTO.getItem().length()));
        } else {
            if (files != null && !files.isEmpty()) {
                togetherDTO.setFiles(files); // 파일 설정
            }
            togetherService.update(togetherDTO);
            map.put("code", 200);
            map.put("msg", MSG_SUCCESS_UPDATE);
            return map;
        }
    }

    /**
     * 같이가요 게시글 삭제
     * 
     * @param id        게시글 ID
     * @param loginUser 로그인 유저
     * @return 삭제 결과
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTogether(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        Map<String, Object> map = new HashMap<>();
        if (!loginUser.getId().equals(togetherService.read(id, loginUser).getUserId())) {
            throw new IllegalArgumentException(ERROR_UNAUTHORIZED_DELETE);
        } else {
            togetherService.delete(id);
            map.put("code", 200);
            map.put("msg", MSG_SUCCESS_DELETE);
            return map;
        }
    }

    /**
     * 같이가요 게시글 목록 조회
     * 
     * @param pageable  페이지 정보
     * @param isEnd     종료 여부
     * @param sortBy    정렬 기준
     * @param addressId 주소 ID
     * @param loginUser 로그인 유저
     * @return 목록 조회 결과
     */
    @GetMapping("/list")
    public Page<TogetherDTO> listAllTogethers(@PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false, defaultValue = "true") Boolean isEnd,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = true, defaultValue = "1100000000") String addressId,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        log.info("addressId : {}", addressId);
        return togetherService.listAll(pageable, isEnd, sortBy, addressId, loginUser.getId());
    }

    /**
     * 같이가요 게시글 검색
     * 
     * @param keyword   검색 키워드
     * @param pageable  페이지 정보
     * @param isEnd     종료 여부
     * @param sortBy    정렬 기준
     * @param addressId 주소 ID
     * @param loginUser 로그인 유저
     * @return 검색 결과
     */
    @GetMapping("/search")
    public Page<TogetherDTO> searchTogethers(@RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") Boolean isEnd,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = true, defaultValue = "1100000000") String addressId,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        return togetherService.searchByKeywords(keyword, pageable, isEnd, sortBy, addressId, loginUser.getId());
    }

    /**
     * 좋아요 목록 조회
     * 
     * @param pageable  페이지 정보
     * @param loginUser 로그인 유저
     * @return 좋아요 목록 조회 결과
     */
    @GetMapping("/favorite/list")
    public Page<TogetherDTO> listFavoriteTogethers(@PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        return togetherService.listByUserIdFavorite(loginUser.getId(), pageable);
    }

    /**
     * 유저 게시글 목록 조회
     * 
     * @param pageable  페이지 정보
     * @param loginUser 로그인 유저
     * @param userId    유저 ID
     * @return 유저 게시글 목록 조회 결과
     */
    @GetMapping("/list/user")
    public Page<TogetherDTO> listByUserId(@PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestParam(required = false) Integer userId) {
        if (userId != null) {
            return togetherService.listByUserId(userId, pageable);
        } else {
            return togetherService.listByUserId(loginUser.getId(), pageable);
        }
    }

    /**
     * 이미지 다운로드
     * 
     * @param id 이미지 ID
     * @return 다운로드 결과
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = togetherService.downloadImage(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    /**
     * 프로필 이미지 다운로드
     * 
     * @param id 프로필 이미지 ID
     * @return 다운로드 결과
     */
    @GetMapping("/downloadprofile")
    public ResponseEntity<Resource> downloadProfile(@RequestParam Integer id) throws Exception {
        File file;
        try {
            file = togetherService.downloadProfileImage(id);
        } catch (FileNotFoundException e) {
            // 파일이 없을 경우: DB에서 프로필 삭제
            togetherService.deleteProfileById(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
