package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.FaqService;
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
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    @PostMapping("/create")
    public Map<String, Object > createFaq(@RequestPart("faqDTO") FaqDTO faqDTO,
                                          @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                          @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if(faqDTO.getTitle().length() > 255 || faqDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + faqDTO.getTitle().length());
        }else if (faqDTO.getContent().length() > 65535) {
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + faqDTO.getContent().length());
        }else{
            if(files != null && !files.isEmpty()){
                faqDTO.setFiles(files);
            }
            faqService.create(faqDTO);
            map.put("code", 200);
            map.put("msg", "자주 묻는 질문 작성에 성공했습니다.");
            return map;
        }
    }

    @GetMapping("/{id}")
    public FaqDTO readFaq(@PathVariable Integer id) {
        return faqService.read(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateFaq(@PathVariable Integer id, @RequestBody FaqDTO faqDTO) throws IOException {
        Map<String, Object> map = new HashMap<>();
        faqDTO.setId(id);
        if(faqDTO.getTitle().length() > 255 || faqDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + faqDTO.getTitle().length());
        }else if (faqDTO.getContent().length() > 65535) {
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + faqDTO.getContent().length());
        }else{
            faqService.update(faqDTO);
            map.put("code", 200);
            map.put("msg", "자주 묻는 질문 수정에 성공했습니다.");
            return map;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteFaq(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        Map<String, Object> map = new HashMap<>();
        if(!loginUser.getId().equals(faqService.read(id).getUserId())) {
            throw new IllegalArgumentException("자주묻는 질문 삭제는 관리자만 가능합니다.");
        }else {
            faqService.delete(id);
            map.put("code", 200);
            map.put("msg", "자주 묻는 질문 삭제에 성공했습니다.");
            return map;
        }
    }

    @GetMapping
    public Page<FaqDTO> listAllFaqs(@PageableDefault(size = 10) Pageable pageable) {
        return faqService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<FaqDTO> searchFaqs(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return faqService.searchByKeywords(keyword, pageable);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = faqService.downloadImage(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
