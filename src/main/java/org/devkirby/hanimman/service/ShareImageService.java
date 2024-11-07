package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareImageDTO;

public interface ShareImageService {
    void create (ShareImageDTO shareImageDTO);
    ShareImageDTO read(Integer id);
    void update (ShareImageDTO shareImageDTO);
    void delete (Integer id);
}
