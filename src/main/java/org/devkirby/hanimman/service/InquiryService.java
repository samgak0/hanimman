package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.InquiryDTO;

public interface InquiryService {
    void create(InquiryDTO inquiryDTO);
    InquiryDTO read(Integer id);
    void update(InquiryDTO inquiryDTO);
    void delete(Integer id);
}
