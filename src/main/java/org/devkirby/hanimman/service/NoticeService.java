package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.NoticeDTO;

public interface NoticeService {
    void create(NoticeDTO noticeDTO);
    NoticeDTO read(Integer id);
    void update(NoticeDTO noticeDTO);
    void delete(Integer id);
}
