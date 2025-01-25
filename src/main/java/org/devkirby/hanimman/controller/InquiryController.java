package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.InquiryService;
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
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/create")
    public Map<String, Object> createInquiry(@RequestPart("inquiryDTO") InquiryDTO inquiryDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal User loginUser) throws IOException {
        validateInquiry(inquiryDTO, files);
        if (files != null && !files.isEmpty()) {
            inquiryDTO.setFiles(files);
        }
        inquiryService.create(inquiryDTO);
        return createResponse(200, "1:1 문의 작성에 성공했습니다.");
    }

    @GetMapping("/{id}")
    public InquiryDTO readInquiry(@PathVariable Integer id) {
        return inquiryService.read(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateInquiry(@PathVariable Integer id, @RequestBody InquiryDTO inquiryDTO,
            @AuthenticationPrincipal User loginUser) throws IOException {
        inquiryDTO.setId(id);
        validateInquiry(inquiryDTO, inquiryDTO.getFiles());
        inquiryDTO.setUserId(loginUser.getId());
        inquiryService.create(inquiryDTO);
        return createResponse(200, "1:1 문의 수정에 성공했습니다.");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteInquiry(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        if (!loginUser.getId().equals(inquiryService.read(id).getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 문의만 삭제할 수 있습니다.");
        }
        inquiryService.delete(id);
        return createResponse(200, "1:1 문의 삭제에 성공했습니다.");
    }

    @GetMapping("/list")
    public Page<InquiryDTO> listAllInquiries(@PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<InquiryDTO> searchInquiries(@RequestParam Integer id, @PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.searchById(id, pageable);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = inquiryService.downloadImage(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private void validateInquiry(InquiryDTO inquiryDTO, List<MultipartFile> files) {
        if (inquiryDTO.getTitle().length() > 255 || inquiryDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + inquiryDTO.getTitle().length());
        } else if (inquiryDTO.getContent().length() > 65535) {
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + inquiryDTO.getContent().length());
        } else if (files != null && files.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수: "
                    + files.size());
        }
    }

    private Map<String, Object> createResponse(int code, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
