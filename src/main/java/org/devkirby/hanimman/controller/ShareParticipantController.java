package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareParticipantDTO;
import org.devkirby.hanimman.service.ShareParticipantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/share-participant")
@RequiredArgsConstructor
public class ShareParticipantController {
    private final ShareParticipantService shareParticipantService;

    @PostMapping("/create")
    public void createParticipant(ShareParticipantDTO shareParticipantDTO) {
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
}
