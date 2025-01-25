package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;

public interface NoticeService {
    void create(NoticeDTO noticeDTO) throws IOException;
    NoticeDTO read(Integer id);
    void update(NoticeDTO noticeDTO) throws IOException;
    void delete(Integer id);

    Page<NoticeDTO> listAll(Pageable pageable);

    Page<NoticeDTO> searchByKeywords(String keyword, Pageable pageable);

    File downloadImage(Integer id) throws IOException;
}
