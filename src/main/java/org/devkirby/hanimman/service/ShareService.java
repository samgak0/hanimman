package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;

public interface ShareService {
    Integer register(ShareDTO shareDTO);
    ShareDTO get(Integer id);
    void modify(ShareDTO shareDTO);
    void remove(Integer id);
}
