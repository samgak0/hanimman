package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherDTO;

public interface TogetherService {
    Integer register(TogetherDTO togetherDTO);
    TogetherDTO get(Integer id);
    void modify(TogetherDTO togetherDTO);
    void remove(Integer id);
}
