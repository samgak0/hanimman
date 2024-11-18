package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.devkirby.hanimman.dto.InquiryRequest;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.InquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping
    public Map<String, Object> createInquiry(@RequestBody InquiryDTO inquiryDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if(inquiryDTO.getTitle().length() > 255 || inquiryDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + inquiryDTO.getTitle().length());
        }else if(inquiryDTO.getContent().length() > 65535) {
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + inquiryDTO.getContent().length());
        }else if(inquiryDTO.getFiles().size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수: "
                    + inquiryDTO.getFiles().size());
        } else {
            inquiryDTO.setUserId(loginUser.getId());
            inquiryService.create(inquiryDTO);
            map.put("code", 200);
            map.put("msg", "1:1 문의 작성에 성공했습니다.");
        }
        return map;
    }

    @GetMapping("/{id}")
    public InquiryDTO readInquiry(@PathVariable Integer id) {
        return inquiryService.read(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateInquiry(@PathVariable Integer id, @RequestBody InquiryDTO inquiryDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        inquiryDTO.setId(id);
        if(inquiryDTO.getTitle().length() > 255 || inquiryDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + inquiryDTO.getTitle().length());
        }else if(inquiryDTO.getContent().length() > 65535) {
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + inquiryDTO.getContent().length());
        }else if(inquiryDTO.getFiles().size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수: "
                    + inquiryDTO.getFiles().size());
        } else {
            inquiryDTO.setUserId(loginUser.getId());
            inquiryService.create(inquiryDTO);
            map.put("code", 200);
            map.put("msg", "1:1 문의 수정에 성공했습니다.");
        }
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteInquiry(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        Map<String, Object> map = new HashMap<>();
        if(!loginUser.getId().equals(inquiryService.read(id).getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 문의만 삭제할 수 있습니다.");
        }else{
            inquiryService.delete(id);
            map.put("code", 200);
            map.put("msg", "1:1 문의 삭제에 성공했습니다.");
        }
        return map;
    }

    @GetMapping
    public Page<InquiryDTO> listAllInquiries(@PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<InquiryDTO> searchInquiries(@RequestParam Integer id, @PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.searchById(id, pageable);
    }
}
