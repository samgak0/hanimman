package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherParticipantDTO;
import org.devkirby.hanimman.service.TogetherParticipantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/together-participant")
@RequiredArgsConstructor
public class TogetherParticipantController {
    private final TogetherParticipantService togetherParticipantService;

    @PostMapping
    public void createParticipant(TogetherParticipantDTO togetherParticipantDTO) {
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
        return togetherParticipantService.listAllByParentIdAndRejectedIsFalse(parentId);
    }
}
