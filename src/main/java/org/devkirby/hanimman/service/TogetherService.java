package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface TogetherService {
    void create(TogetherDTO togetherDTO) throws IOException;
    TogetherDTO read(Integer id, User loginUser);
    void update(TogetherDTO togetherDTO) throws IOException;
    void delete(Integer id);

    Page<TogetherDTO> listAll(Pageable pageable, Boolean isEnd, String sortBy);;

    Page<TogetherDTO> searchByKeywords(String keyword, Pageable pageable, Boolean isEnd, String sortBy);

    void updateIsEnd();

    File downloadImage(Integer id) throws IOException;
}
