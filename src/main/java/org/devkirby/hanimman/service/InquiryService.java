package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryService {
    void create(InquiryDTO inquiryDTO, InquiryFileDTO inquiryFileDTO);
    InquiryDTO read(Integer id);
    void update(InquiryDTO inquiryDTO);
    void delete(Integer id);

    Page<InquiryDTO> listAll(Pageable pageable);

    Page<InquiryDTO> searchById(Integer id, Pageable pageable);
}
