package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShareService {
    void create(ShareDTO shareDTO);
    ShareDTO read(Integer id);
    void update(ShareDTO shareDTO);
    void delete(Integer id);

    Page<ShareDTO> listAll(Pageable pageable);
}
