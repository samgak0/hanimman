package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.dto.FaqFileDTO;
import org.devkirby.hanimman.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface FaqService {
    void create(FaqDTO faqDTO) throws IOException;
    FaqDTO read(Integer id);
    void update(FaqDTO faqDTO) throws IOException;
    void delete(Integer id);

    Page<FaqDTO> listAll(Pageable pageable);

    Page<FaqDTO> searchByKeywords(String keyword, Pageable pageable);
}
