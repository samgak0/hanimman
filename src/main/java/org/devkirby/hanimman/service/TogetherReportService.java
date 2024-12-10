package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.TogetherReportDTO;
import org.devkirby.hanimman.entity.ReportCategory;
import org.devkirby.hanimman.entity.TogetherReport;

import java.util.List;

public interface TogetherReportService {
//  void create(Integer reporterId, Integer categoryId, Integer parentId);
    void delete(Integer id);
    void create(TogetherReportDTO togetherReportDTO);

//  List<TogetherReport> findAllDeletedReports();

  List<TogetherReport> findAllSelectReports();

//    List<TogetherReportDTO> findAllReports();

    List<ReportCategoryDTO> findAllCategories();
}
