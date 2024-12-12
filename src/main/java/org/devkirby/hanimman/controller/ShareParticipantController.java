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

    @PostMapping("/create")
    public void createParticipant(@RequestBody ShareParticipantDTO shareParticipantDTO,
                                  @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareParticipantDTO.setUserId(loginUser.getId());
        shareParticipantService.create(shareParticipantDTO);
    }

    @GetMapping("/{id}")
    public ShareParticipantDTO readParticipant(Integer id) {
        return shareParticipantService.read(id);
    }

    @PutMapping("/{id}")
    public void updateParticipant(ShareParticipantDTO shareParticipantDTO) {
        shareParticipantService.update(shareParticipantDTO);
    }

    @PutMapping("/{id}/rejected")
    public void rejectedParticipant(Integer id) {
        shareParticipantService.rejected(id);
    }

    @PutMapping("/{id}/accepted")
    public void acceptedParticipant(Integer id) {
        shareParticipantService.accepted(id);
    }

    @PutMapping("/{id}/complete")
    public void completeParticipant(Integer id) {
        shareParticipantService.complete(id);
    }

    @DeleteMapping("/{id}")
    public void deleteParticipant(Integer id) {
        shareParticipantService.delete(id);
    }

    @GetMapping("/list/{parentId}")
    public List<ShareParticipantDTO> listAllByParentId(Integer parentId) {
        return shareParticipantService.listAllByParentId(parentId);
    }

    @GetMapping("/list/{parentId}/rejected-false")
    public List<ShareParticipantDTO> listAllByParentIdAndRejectedIsFalse(Integer parentId) {
        return shareParticipantService.listAllByParentIdAndRejectedIsNull(parentId);
    }

    @GetMapping("/list/user")
    public List<ShareParticipantDTO> listAllByUserId(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return shareParticipantService.listAllByUserId(loginUser.getId());
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = shareService.downloadImage(id);
        InputStreamResource resource =
                new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
