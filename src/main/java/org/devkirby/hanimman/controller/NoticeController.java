package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeDTO;
import org.devkirby.hanimman.dto.NoticeFileDTO;
import org.devkirby.hanimman.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public void createNotice(@RequestBody NoticeDTO noticeDTO, @RequestBody NoticeFileDTO noticeFileDTO) {
        noticeService.create(noticeDTO, noticeFileDTO);
    }

    @GetMapping("/{id}")
    public NoticeDTO readNotice(@PathVariable Integer id) {
        return noticeService.read(id);
    }

    @PutMapping("/{id}")
    public void updateNotice(@PathVariable Integer id, @RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(id);
        noticeService.update(noticeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable Integer id) {
        noticeService.delete(id);
    }

    @GetMapping
    public Page<NoticeDTO> listAllNotices(@PageableDefault(size = 10) Pageable pageable) {
        return noticeService.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<NoticeDTO> searchNotices(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return noticeService.searchByKeywords(keyword, pageable);
    }
}
