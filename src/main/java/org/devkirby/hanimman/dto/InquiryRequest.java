package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequest {
    private InquiryDTO inquiryDTO;
    private InquiryFileDTO inquiryFileDTO;
}
