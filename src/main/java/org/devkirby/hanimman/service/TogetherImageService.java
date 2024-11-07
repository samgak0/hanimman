package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherImageDTO;

public interface TogetherImageService {
    void create(TogetherImageDTO togetherImageDTO);
    TogetherImageDTO read(Integer id);
    void update(TogetherImageDTO togetherImageDTO);
    void delete(Integer id);
}
