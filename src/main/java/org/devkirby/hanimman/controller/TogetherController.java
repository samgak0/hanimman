package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.service.TogetherService;
import org.devkirby.hanimman.service.UserAddressService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;
    private final Logger log = LoggerFactory.getLogger(TogetherController.class);
    private final UserAddressService userAddressService;

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
            throw new IllegalStateException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이 : " +
                    +togetherDTO.getTitle().length());
        } else if (togetherDTO.getContent().length() > 1000 || togetherDTO.getContent().isEmpty()) {
            throw new IllegalStateException("내용의 길이는 65535자 이하여야 합니다. 현재 길이 : " +
                    +togetherDTO.getContent().length());
        } else if (files != null && files.size() > 10) {
            throw new IllegalStateException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수 : " +
                    +files.size());
        } else if (togetherDTO.getMeetingAt().isBefore(oneHourLater) || togetherDTO.getMeetingAt().isAfter(limitDay)) {
            throw new IllegalStateException("같이가요 시간은 현재 시간으로부터 한 시간 이후, 7일 이전이어야 합니다.");
        } else if (togetherDTO.getQuantity() < 1) {
            throw new IllegalStateException("수량은 1개 이상이어야 합니다.");
        } else if (togetherDTO.getPrice() < 0) {
            throw new IllegalStateException("가격은 0원 이상이어야 합니다.");
        } else if (togetherDTO.getItem().isEmpty() || togetherDTO.getItem().length() > 50) {
            throw new IllegalStateException("물품의 길이는 1자 이상, 50자 이하여야 합니다. 현재 길이 : " +
                    +togetherDTO.getItem().length());
        } else {
            togetherDTO.setUserId(loginUser.getId());
            if (files != null && !files.isEmpty()) {
                togetherDTO.setFiles(files); // 파일 설정
            }
            Integer id = togetherService.create(togetherDTO, primaryAddressId);
            map.put("code", 200);
            map.put("id", id);
            map.put("msg", "같이가요 게시글 작성에 성공했습니다.");
            return map;
        }
    }

    @GetMapping("/{id}")
    public TogetherDTO readTogether(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        return togetherService.read(id, loginUser);
    }

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
        log.info("수정테스트입니다. {}", togetherDTO);
        if (!loginUser.getId().equals(togetherDTO.getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        } else if (togetherDTO.getTitle().length() > 255 || togetherDTO.getTitle().isEmpty()) {
            throw new IllegalStateException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이 : " +
                    +togetherDTO.getTitle().length());
        } else if (togetherDTO.getContent().length() > 65535 || togetherDTO.getContent().isEmpty()) {
            throw new IllegalStateException("내용의 길이는 65535자 이하여야 합니다. 현재 길이 : " +
                    +togetherDTO.getContent().length());
        } else if (togetherDTO.getMeetingAt().isBefore(oneHourLater) || togetherDTO.getMeetingAt().isAfter(limitDay)) {
            throw new IllegalStateException("같이가요 시간은 현재 시간으로부터 한 시간 이후, 7일 이전이어야 합니다.");
        } else if (togetherDTO.getQuantity() < 1) {
            throw new IllegalStateException("수량은 1개 이상이어야 합니다.");
        } else if (togetherDTO.getPrice() < 0) {
            throw new IllegalStateException("가격은 0원 이상이어야 합니다.");
        } else if (togetherDTO.getItem().isEmpty() || togetherDTO.getItem().length() > 50) {
            throw new IllegalStateException("물품의 길이는 1자 이상, 50자 이하여야 합니다. 현재 길이 : " +
                    +togetherDTO.getItem().length());
        } else {
            if (files != null && !files.isEmpty()) {
                togetherDTO.setFiles(files); // 파일 설정
            }
            togetherService.update(togetherDTO);
            map.put("code", 200);
            map.put("msg", "같이가요 게시글 수정에 성공했습니다.");
            return map;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTogether(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        Map<String, Object> map = new HashMap<>();
        if (!loginUser.getId().equals(togetherService.read(id, loginUser).getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        } else {
            togetherService.delete(id);
            map.put("code", 200);
            map.put("msg", "같이가요 게시글 삭제에 성공했습니다.");
            return map;
        }
    }

    @GetMapping("/list")
    public Page<TogetherDTO> listAllTogethers(@PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false, defaultValue = "true") Boolean isEnd,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = true, defaultValue = "1100000000") String addressId,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        log.info("addressId : {}", addressId);
        return togetherService.listAll(pageable, isEnd, sortBy, addressId, loginUser.getId());
    }

    @GetMapping("/search")
    public Page<TogetherDTO> searchTogethers(@RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") Boolean isEnd,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = true, defaultValue = "1100000000") String addressId,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        return togetherService.searchByKeywords(keyword, pageable, isEnd, sortBy, addressId, loginUser.getId());
    }

    @GetMapping("/favorite/list")
    public Page<TogetherDTO> listFavoriteTogethers(@PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        return togetherService.listByUserIdFavorite(loginUser.getId(), pageable);
    }

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
