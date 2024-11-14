package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareRequest {
    private ShareDTO shareDTO;
    private List<ShareImageDTO> shareImageDTO;
}