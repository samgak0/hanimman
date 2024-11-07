package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;

public interface ShareService {
    void create(ShareDTO shareDTO);
    ShareDTO read(Integer id);
    void update(ShareDTO shareDTO);
    void delete(Integer id);
}
