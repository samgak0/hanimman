package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.service.TogetherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;

    @PostMapping
    public void createTogether(@RequestBody TogetherDTO togetherDTO) {
        togetherService.create(togetherDTO);
    }

    @GetMapping("/{id}")
    public TogetherDTO readTogether(@PathVariable Integer id) {
        return togetherService.read(id);
    }

    @PutMapping("/{id}")
    public void updateTogether(@PathVariable Integer id, @RequestBody TogetherDTO togetherDTO) {
        togetherDTO.setId(id);
        togetherService.update(togetherDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTogether(@PathVariable Integer id) {
        togetherService.delete(id);
    }

    @GetMapping
    public Page<TogetherDTO> listAllTogethers(@PageableDefault(size = 10)Pageable pageable) {
        return togetherService.listAll(pageable);
    }
}
