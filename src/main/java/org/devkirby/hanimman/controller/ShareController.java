package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.ShareService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/share")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;

    @PostMapping("/create")
    public Map<String, Object> createShare(@RequestPart("togetherDTO") ShareDTO shareDTO,
                                           @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                           @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Instant now = Instant.now();
        Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);
        Instant limitDay = now.plus(7, ChronoUnit.DAYS);
        if(shareDTO.getTitle().length() > 255 || shareDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getTitle().length());
        }else if(shareDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getContent().length());
        }else if(files != null && files.size()>10){
            throw new IllegalArgumentException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수: "
                    + shareDTO.getFiles().size());
        } else if (shareDTO.getLocationDate().isBefore(oneHourLater) || shareDTO.getLocationDate().isAfter(limitDay)) {
            throw new IllegalArgumentException("나눠요 시간은 현재 시간으로부터 한 시간 이후, 7일 이전이어야 합니다.");
        } else {
            shareDTO.setUserId(loginUser.getId());
            if(files != null && !files.isEmpty()){
                shareDTO.setFiles(files);
            }
            shareService.create(shareDTO);
            map.put("code", 200);
            map.put("msg", "나눠요 게시글 작성에 성공했습니다.");
        }
        return map;
    }

    @GetMapping("/{id}")
    public ShareDTO readShare(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        return shareService.read(id, loginUser);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateShare(@PathVariable Integer id, @RequestBody ShareDTO shareDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        shareDTO.setId(id);
        Instant now = Instant.now();
        Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);
        Instant limitDay = now.plus(7, ChronoUnit.DAYS);
        if(!loginUser.getId().equals(shareDTO.getUserId())){
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }if(shareDTO.getTitle().length() > 255 || shareDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getTitle().length());
        }else if(shareDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getContent().length());
        } else if (shareDTO.getLocationDate().isBefore(oneHourLater) || shareDTO.getLocationDate().isAfter(limitDay)) {
            throw new IllegalArgumentException("나눠요 시간은 현재 시간으로부터 한 시간 이후, 7일 이전이어야 합니다.");
        }else{
            shareService.update(shareDTO);
            map.put("code", 200);
            map.put("msg", "나눠요 게시글 수정에 성공했습니다.");
        }
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteShare(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        Map<String, Object> map = new HashMap<>();
        if(!loginUser.getId().equals(shareService.read(id, loginUser).getUserId())){
            throw new IllegalArgumentException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }else{
            shareService.delete(id);
            map.put("code", 200);
            map.put("msg", "나눠요 게시글 삭제에 성공했습니다.");
        }
        return map;
    }

    @GetMapping("/list")
    public Page<ShareDTO> listAllShares(@PageableDefault(size = 10) Pageable pageable,
                                        @RequestParam(required = false, defaultValue = "false") Boolean isEnd,
                                        @RequestParam(required = false, defaultValue = "createdAt") String sortBy) {
        return shareService.listAll(pageable, isEnd, sortBy);
    }

    @GetMapping("/search")
    public Page<ShareDTO> searchShares(@PageableDefault(size = 10) Pageable pageable,
                                       @RequestParam String keyword,
                                       @RequestParam(required = false, defaultValue = "false") Boolean isEnd,
                                       @RequestParam(required = false, defaultValue = "createdAt") String sortBy) {
        return shareService.searchByKeywords(keyword, pageable, isEnd, sortBy);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = shareService.downloadImage(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
