package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeDTO;
import org.devkirby.hanimman.dto.NoticeFileDTO;
import org.devkirby.hanimman.dto.NoticeRequest;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.NoticeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public Map<String, Object> createNotice(@RequestPart("noticeDTO") NoticeDTO noticeDTO,
                                            @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                            @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if(noticeDTO.getTitle().length() > 255 || noticeDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + noticeDTO.getTitle().length());
        }else if(noticeDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + noticeDTO.getContent().length());
        }else{
            if(files != null && files.isEmpty()){
                noticeDTO.setFiles(files);
            }
            noticeService.create(noticeDTO);
            map.put("code", 200);
            map.put("msg", "공지사항 작성에 성공했습니다.");
        }
        return map;
    }

    @GetMapping("/{id}")
    public NoticeDTO readNotice(@PathVariable Integer id) {
        return noticeService.read(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateNotice(@PathVariable Integer id, @RequestBody NoticeDTO noticeDTO) throws IOException {
        Map<String, Object> map = new HashMap<>();

        if(noticeDTO.getTitle().length() > 255 || noticeDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + noticeDTO.getTitle().length());
        }else if(noticeDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + noticeDTO.getContent().length());
        }else{
            noticeService.update(noticeDTO);
            map.put("code", 200);
            map.put("msg", "공지사항 수정에 성공했습니다.");
        }
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteNotice(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        Map<String, Object> map = new HashMap<>();
        if(!loginUser.getId().equals(noticeService.read(id).getUserId())){
            throw new IllegalArgumentException("공지사항은 관리자만 삭제할 수 있습니다.");
        }else{
            noticeService.delete(id);
            map.put("code", 200);
            map.put("msg", "공지사항 삭제에 성공했습니다.");
            return map;
        }
    }

    @GetMapping
    public Page<NoticeDTO> listAllNotices(@PageableDefault(size = 10) Pageable pageable) {
        return noticeService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<NoticeDTO> searchNotices(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return noticeService.searchByKeywords(keyword, pageable);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = noticeService.downloadImage(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
