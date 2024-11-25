package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherReportDTO;

public interface TogetherReportService {
//    void create(Integer reporterId, Integer categoryId, Integer parentId);
    void delete(Integer id);
    void create(TogetherReportDTO togetherReportDTO);

}
