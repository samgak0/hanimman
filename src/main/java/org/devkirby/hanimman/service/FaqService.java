package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.dto.NoticeDTO;

import java.util.List;

public interface FaqService {
    void create(FaqDTO faqDTO);
    FaqDTO read(Integer id);
    void update(FaqDTO faqDTO);
    void delete(Integer id);

    List<FaqDTO> listAll();
}
