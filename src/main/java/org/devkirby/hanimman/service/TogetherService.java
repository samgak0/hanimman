package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherDTO;

public interface TogetherService {
    void create(TogetherDTO togetherDTO);
    TogetherDTO read(Integer id);
    void update(TogetherDTO togetherDTO);
    void delete(Integer id);
}
