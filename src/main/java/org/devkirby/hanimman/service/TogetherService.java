package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TogetherService {
    void create(TogetherDTO togetherDTO);
    TogetherDTO read(Integer id);
    void update(TogetherDTO togetherDTO);
    void delete(Integer id);

    Page<TogetherDTO> listAll(Pageable pageable);;
}
