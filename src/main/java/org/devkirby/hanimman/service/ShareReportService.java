package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareReportDTO;

public interface ShareReportService {
    void create(ShareReportDTO shareReportDTO);
    void delete(Integer id);
}
