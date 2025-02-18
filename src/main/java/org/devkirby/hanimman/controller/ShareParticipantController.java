package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ShareParticipantDTO;
import org.devkirby.hanimman.service.ShareParticipantService;
import org.devkirby.hanimman.service.ShareService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/v1/share-participant")
@RequiredArgsConstructor
public class ShareParticipantController {
    private final ShareParticipantService shareParticipantService;
    private final ShareService shareService;

    /**
     * 나눠요 게시글 참여자 생성
     * 
     * @param shareParticipantDTO 참여자 정보
     * @param loginUser           로그인 유저
     */
    @PostMapping("/create")
    public void createParticipant(@RequestBody ShareParticipantDTO shareParticipantDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareParticipantDTO.setUserId(loginUser.getId());
        shareParticipantService.create(shareParticipantDTO);
    }

    /**
     * 나눠요 게시글 참여자 조회
     * 
     * @param id 참여자 아이디
     * @return 참여자 정보
     */
    @GetMapping("/{id}")
    public ShareParticipantDTO readParticipant(Integer id) {
        return shareParticipantService.read(id);
    }

    /**
     * 나눠요 게시글 참여자 수정
     * 
     * @param shareParticipantDTO 참여자 정보
     */
    @PutMapping("/{id}")
    public void updateParticipant(ShareParticipantDTO shareParticipantDTO) {
        shareParticipantService.update(shareParticipantDTO);
    }

    /**
     * 나눠요 게시글 참여자 거절
     * 
     * @param id 참여자 아이디
     */
    @PutMapping("/{id}/rejected")
    public void rejectedParticipant(@PathVariable Integer id) {
        shareParticipantService.rejected(id);
    }

    /**
     * 나눠요 게시글 참여자 승인
     * 
     * @param id 참여자 아이디
     */
    @PutMapping("/{id}/accepted")
    public void acceptedParticipant(@PathVariable Integer id) {
        shareParticipantService.accepted(id);
    }

    /**
     * 나눠요 게시글 참여자 완료
     * 
     * @param id 참여자 아이디
     */
    @PutMapping("/{id}/complete")
    public void completeParticipant(@PathVariable Integer id) {
        shareParticipantService.complete(id);
    }

    /**
     * 나눠요 게시글 참여자 삭제
     * 
     * @param id 참여자 아이디
     */
    @DeleteMapping("/{id}")
    public void deleteParticipant(Integer id) {
        shareParticipantService.delete(id);
    }

    /**
     * 나눠요 게시글 참여자 목록 조회
     * 
     * @param parentId 나눠요 게시글 아이디
     * @return 참여자 목록
     */
    @GetMapping("/list/{parentId}")
    public List<ShareParticipantDTO> listAllByParentId(@PathVariable Integer parentId) {
        return shareParticipantService.listAllByParentId(parentId);
    }

    /**
     * 나눠요 게시글 참여자 목록 조회
     * 
     * @param parentId 나눠요 게시글 아이디
     * @return 참여자 목록
     */
    @GetMapping("/list/{parentId}/rejected-false")
    public List<ShareParticipantDTO> listAllByParentIdAndRejectedIsFalse(Integer parentId) {
        return shareParticipantService.listAllByParentIdAndRejectedIsNull(parentId);
    }

    /**
     * 나눠요 게시글 참여자 목록 조회
     * 
     * @param loginUser 로그인 유저
     * @return 참여자 목록
     */
    @GetMapping("/list/user")
    public List<ShareParticipantDTO> listAllByUserId(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return shareParticipantService.listAllByUserId(loginUser.getId());
    }

    /**
     * 나눠요 게시글 참여자 다운로드
     * 
     * @param id 참여자 아이디
     * @return 참여자 정보
     */
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
