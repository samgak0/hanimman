package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.dto.ShareRequest;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.ShareService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/share")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;

    @PostMapping
    public Map<String, Object> createShare(@RequestBody ShareRequest shareRequest) {
        Map<String, Object> map = new HashMap<>();
        if(shareRequest.getShareDTO().getTitle().length() > 255 || shareRequest.getShareDTO().getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1byte 이상, 255byte 이하여야 합니다. 현재 길이: "
                    + shareRequest.getShareDTO().getTitle().length());
        }else if(shareRequest.getShareDTO().getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535byte 이하여야 합니다. 현재 길이: "
                    + shareRequest.getShareDTO().getContent().length());
        }else{
            shareService.create(shareRequest.getShareDTO(), shareRequest.getShareImageDTO());
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
