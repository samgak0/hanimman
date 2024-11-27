package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;

public interface ShareService {
    void create(ShareDTO shareDTO) throws IOException;
    ShareDTO read(Integer id, User loginUser);
    void update(ShareDTO shareDTO) throws IOException;
    void delete(Integer id);

    Page<ShareDTO> listAll(Pageable pageable, Boolean isEnd, String sortBy);

    Page<ShareDTO> searchByKeywords(String keyword, Pageable pageable, Boolean isEnd, String sortBy);

    void updateIsEnd();

    File downloadImage(Integer id) throws IOException;
}
