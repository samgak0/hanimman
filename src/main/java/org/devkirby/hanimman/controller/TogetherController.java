package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.dto.TogetherRequest;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.TogetherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;

    @PostMapping
    public Map<String, Object> createTogether(@RequestBody TogetherDTO togetherDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Instant now = Instant.now();
        Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);
        Instant limitDay = now.plus(15, ChronoUnit.DAYS);
        if(togetherDTO.getTitle().length() > 255 || togetherDTO.getTitle().isEmpty()){
            throw new IllegalStateException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이 : " +
                    + togetherDTO.getTitle().length());
        }else if(togetherDTO.getContent().length() > 1000){
            throw new IllegalStateException("내용의 길이는 65535자 이하여야 합니다. 현재 길이 : " +
                    + togetherDTO.getContent().length());
        }else if(togetherDTO.getFiles().size()>10) {
            throw new IllegalStateException("이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수 : " +
                    +togetherDTO.getFiles().size());
        }else if(togetherDTO.getMeetingAt().isBefore(oneHourLater) || togetherDTO.getMeetingAt().isAfter(limitDay)) {
            throw new IllegalStateException("같이가요 시간은 현재 시간으로부터 한 시간 이후, 15일 이전이어야 합니다.");
        } else {
            togetherDTO.setUserId(loginUser.getId());
            togetherService.create(togetherDTO);
            map.put("code", 200);
            map.put("msg", "같이가요 게시글 작성에 성공했습니다.");
            return map;
        }
    }

    @GetMapping("/{id}")
    public TogetherDTO readTogether(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        return togetherService.read(id, loginUser);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateTogether(@PathVariable Integer id, @RequestBody TogetherDTO togetherDTO, @AuthenticationPrincipal User loginUser) throws IOException {
        Map<String, Object> map = new HashMap<>();
        togetherDTO.setId(id);
        Instant now = Instant.now();
        Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);
        Instant limitDay = now.plus(15, ChronoUnit.DAYS);
        if(!loginUser.getId().equals(togetherDTO.getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }else if(togetherDTO.getTitle().length() > 255 || togetherDTO.getTitle().isEmpty()){
            throw new IllegalStateException("제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이 : " +
                    + togetherDTO.getTitle().length());
        }else if(togetherDTO.getContent().length() > 65535){
            throw new IllegalStateException("내용의 길이는 65535자 이하여야 합니다. 현재 길이 : " +
                    + togetherDTO.getContent().length());
        }else if(togetherDTO.getMeetingAt().isBefore(oneHourLater) || togetherDTO.getMeetingAt().isAfter(limitDay)) {
            throw new IllegalStateException("같이가요 시간은 현재 시간으로부터 한 시간 이후, 15일 이전이어야 합니다.");
        }else {
            togetherService.update(togetherDTO);
            map.put("code", 200);
            map.put("msg", "같이가요 게시글 수정에 성공했습니다.");
            return map;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTogether(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        Map<String, Object> map = new HashMap<>();
        if(!loginUser.getId().equals(togetherService.read(id, loginUser).getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }else{
            togetherService.delete(id);
            map.put("code", 200);
            map.put("msg", "같이가요 게시글 삭제에 성공했습니다.");
            return map;
        }
    }

    @GetMapping
    public Page<TogetherDTO> listAllTogethers(@PageableDefault(size = 10)Pageable pageable, @RequestParam(required = false) Boolean isEnd) {
        return togetherService.listAll(pageable, isEnd);
    }

    @GetMapping("/search")
    public Page<TogetherDTO> searchTogethers(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return togetherService.searchByKeywords(keyword, pageable);
    }

    /*
    @GetMapping("/not-end")
    public Page<TogetherDTO> listNotEndTogethers(@PageableDefault(size = 10) Pageable pageable) {
        return togetherService.listNotEnd(pageable);
    }
     */
}
