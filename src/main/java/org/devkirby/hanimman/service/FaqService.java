package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.FaqDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;

public interface FaqService {
    void create(FaqDTO faqDTO) throws IOException;
    FaqDTO read(Integer id);
    void update(FaqDTO faqDTO) throws IOException;
    void delete(Integer id);

    Page<FaqDTO> listAll(Pageable pageable);

    Page<FaqDTO> searchByKeywords(String keyword, Pageable pageable);

    File downloadImage(Integer id) throws IOException;
}
