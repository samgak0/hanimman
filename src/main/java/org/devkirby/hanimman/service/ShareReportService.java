package org.devkirby.hanimman.service;

public interface ShareReportService {
    void create(Integer reporterId, Integer categoryId, Integer parentId);
    void delete(Integer id);
}
