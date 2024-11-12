package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShareService {
    void create(ShareDTO shareDTO, ShareImageDTO shareImageDTO);
    ShareDTO read(Integer id, User loginUser);
    void update(ShareDTO shareDTO);
    void delete(Integer id);

    Page<ShareDTO> listAll(Pageable pageable);

    Page<ShareDTO> searchByKeywords(String keyword, Pageable pageable);
}
