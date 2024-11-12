package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.devkirby.hanimman.dto.InquiryRequest;
import org.devkirby.hanimman.service.InquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping
    public void createInquiry(@RequestBody InquiryRequest inquiryRequest) {
        inquiryService.create(inquiryRequest.getInquiryDTO(), inquiryRequest.getInquiryFileDTO());
    }

    @GetMapping("/{id}")
    public InquiryDTO readInquiry(@PathVariable Integer id) {
        return inquiryService.read(id);
    }

    @PutMapping("/{id}")
    public void updateInquiry(@PathVariable Integer id, @RequestBody InquiryDTO inquiryDTO) {
        inquiryDTO.setId(id);
        inquiryService.update(inquiryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteInquiry(@PathVariable Integer id) {
        inquiryService.delete(id);
    }

    @GetMapping
    public Page<InquiryDTO> listAllInquiries(@PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<InquiryDTO> searchInquiries(@RequestParam Integer id, @PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.searchById(id, pageable);
    }
}
