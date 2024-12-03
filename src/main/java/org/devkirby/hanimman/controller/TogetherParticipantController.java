package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherParticipantDTO;
import org.devkirby.hanimman.service.TogetherParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/together-participant")
@RequiredArgsConstructor
public class TogetherParticipantController {
    private final TogetherParticipantService togetherParticipantService;
    private final Logger log = LoggerFactory.getLogger(TogetherParticipantController.class);

    @PostMapping("/create")
    public void createParticipant(@RequestBody
                                      TogetherParticipantDTO togetherParticipantDTO,
                                  @AuthenticationPrincipal CustomUserDetails loginUser) {
        log.info("같이가요 참여 : " + loginUser.getId());
        togetherParticipantDTO.setUserId(loginUser.getId());
        togetherParticipantService.create(togetherParticipantDTO);
    }

    @GetMapping("/{id}")
    public TogetherParticipantDTO readParticipant(Integer id) {
        return togetherParticipantService.read(id);
    }

    @PutMapping("/{id}")
    public void updateParticipant(TogetherParticipantDTO togetherParticipantDTO) {
        togetherParticipantService.update(togetherParticipantDTO);
    }

    @PutMapping("/{id}/rejected")
    public void rejectedParticipant(Integer id) {
        togetherParticipantService.rejected(id);
    }

    @PutMapping("/{id}/accepted")
    public void acceptedParticipant(Integer id) {
        togetherParticipantService.accepted(id);
    }

    @PutMapping("/{id}/complete")
    public void completeParticipant(Integer id) {
        togetherParticipantService.complete(id);
    }

    @DeleteMapping("/{id}")
    public void deleteParticipant(Integer id) {
        togetherParticipantService.delete(id);
    }

    @GetMapping("/list/{parentId}")
    public List<TogetherParticipantDTO> listAllByParentId(Integer parentId) {
        return togetherParticipantService.listAllByParentId(parentId);
    }

    @GetMapping("/list/{parentId}/rejected-false")
    public List<TogetherParticipantDTO> listAllByParentIdAndRejectedIsFalse(Integer parentId) {
        return togetherParticipantService.listAllByParentIdAndRejectedIsNull(parentId);
    }
}
