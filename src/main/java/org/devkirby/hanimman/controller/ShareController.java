package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareRequest;
import org.devkirby.hanimman.entity.Share;
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

    @PostMapping
    public Map<String, Object> createShare(@RequestBody ShareDTO shareDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if(shareDTO.getTitle().length() > 255 || shareDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getTitle().length());
        }else if(shareDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getContent().length());
        }else if(shareDTO.getFiles().size()>10){
            throw new IllegalArgumentException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수: "
                    + shareDTO.getFiles().size());
        } else {
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
    public Map<String, Object> updateShare(@PathVariable Integer id, @RequestBody ShareDTO shareDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        shareDTO.setId(id);
        if(!loginUser.getId().equals(shareDTO.getUserId())){
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }if(shareDTO.getTitle().length() > 255 || shareDTO.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getTitle().length());
        }else if(shareDTO.getContent().length() > 65535){
            throw new IllegalArgumentException("내용의 길이는 65535자 이하여야 합니다. 현재 길이: "
                    + shareDTO.getContent().length());
        }else{
            shareService.update(shareDTO);
            map.put("code", 200);
            map.put("msg", "나눠요 게시글 수정에 성공했습니다.");
            return map;
        }
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
            return map;
        }
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
