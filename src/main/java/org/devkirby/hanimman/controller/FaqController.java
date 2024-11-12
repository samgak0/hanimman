package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.dto.FaqFileDTO;
import org.devkirby.hanimman.dto.FaqRequest;
import org.devkirby.hanimman.service.FaqService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    @PostMapping
    public void createFaq(@RequestBody FaqRequest faqRequest) {
        faqService.create(faqRequest.getFaqDTO(), faqRequest.getFaqFileDTO());
    }

    @GetMapping("/{id}")
    public FaqDTO readFaq(@PathVariable Integer id) {
        return faqService.read(id);
    }

    @PutMapping("/{id}")
    public void updateFaq(@PathVariable Integer id, @RequestBody FaqDTO faqDTO) {
        faqDTO.setId(id);
        faqService.update(faqDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable Integer id) {
        faqService.delete(id);
    }

    @GetMapping
    public Page<FaqDTO> listAllFaqs(@PageableDefault(size = 10) Pageable pageable) {
        return faqService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<FaqDTO> searchFaqs(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return faqService.searchByKeywords(keyword, pageable);
    }
}
