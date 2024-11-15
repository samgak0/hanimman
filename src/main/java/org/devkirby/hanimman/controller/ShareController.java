package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareRequest;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.ShareImageService;
import org.devkirby.hanimman.service.ShareService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/share")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;
    private final ShareImageService shareImageService;

    @PostMapping
    public Map<String, Object> createShare(@RequestBody ShareDTO shareDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if(shareDTO.getTitle().length() > 255 || shareDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1byte 이상, 255byte 이하여야 합니다. 현재 길이: "
                    + shareDTO.getTitle().length());
        }else if(shareDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535byte 이하여야 합니다. 현재 길이: "
                    + shareDTO.getContent().length());
        }else{
            List<MultipartFile> files = shareDTO.getFiles();
            shareDTO.setUserId(loginUser.getId());
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
    public void updateShare(@PathVariable Integer id, @RequestBody ShareRequest shareRequest) {
        shareRequest.getShareDTO().setId(id);
        shareService.update(shareRequest.getShareDTO(), shareRequest.getShareImageDTO());
    }

    @DeleteMapping("/{id}")
    public void deleteShare(@PathVariable Integer id) {
        shareService.delete(id);
    }

    @GetMapping
    public Page<ShareDTO> listAllShares(@PageableDefault(size = 10) Pageable pageable, @RequestBody(required = false) Boolean isEnd) {
        return shareService.listAll(pageable, isEnd);
    }

    @GetMapping("/search")
    public Page<ShareDTO> searchShares(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return shareService.searchByKeywords(keyword, pageable);
    }

    /*
    @GetMapping("/not-end")
    public Page<ShareDTO> listNotEndShares(@PageableDefault(size = 10) Pageable pageable) {
        return shareService.listNotEnd(pageable);
    }
     */
}
