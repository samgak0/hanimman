package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface InquiryService {
    void create(InquiryDTO inquiryDTO) throws IOException;
    InquiryDTO read(Integer id);
    void update(InquiryDTO inquiryDTO) throws IOException;
    void delete(Integer id);

    Page<InquiryDTO> listAll(Pageable pageable);

    Page<InquiryDTO> searchById(Integer id, Pageable pageable);
}
