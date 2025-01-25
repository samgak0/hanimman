package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.TogetherReportDTO;
import org.devkirby.hanimman.entity.TogetherReport;

import java.util.List;

public interface TogetherReportService {
    void delete(Integer id);
    void create(TogetherReportDTO togetherReportDTO);
    List<TogetherReport> findAllSelectReports();
    List<ReportCategoryDTO> findAllCategories();
}
