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

@RestController
@RequestMapping("/api/v1/share")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;

    @PostMapping
    public void createShare(@RequestBody ShareRequest shareRequest) {
        shareService.create(shareRequest.getShareDTO(), shareRequest.getShareImageDTO());
    }

    @GetMapping("/{id}")
    public ShareDTO readShare(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        return shareService.read(id, loginUser);
    }

    @PutMapping("/{id}")
    public void updateShare(@PathVariable Integer id, @RequestBody ShareDTO shareDTO) {
        shareDTO.setId(id);
        shareService.update(shareDTO);
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
