package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TogetherRequest {
    private TogetherDTO togetherDTO;
    private TogetherImageDTO togetherImageDTO;
}
