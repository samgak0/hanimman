package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.ShareReportDTO;
import org.devkirby.hanimman.entity.ReportCategory;

import java.util.List;

public interface ShareReportService {
    void create(ShareReportDTO shareReportDTO);
    void delete(Integer id);

    List<ReportCategoryDTO> findAllCategories();
}
