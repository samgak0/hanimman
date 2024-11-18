package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface TogetherService {
    void create(TogetherDTO togetherDTO) throws IOException;
    TogetherDTO read(Integer id, User loginUser);
    void update(TogetherDTO togetherDTO) throws IOException;
    void delete(Integer id);

    Page<TogetherDTO> listAll(Pageable pageable, Boolean isEnd);;

    Page<TogetherDTO> searchByKeywords(String keyword, Pageable pageable);

//    Page<TogetherDTO> listNotEnd(Pageable pageable);
}
