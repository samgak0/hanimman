package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ReportCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCategoryRepository extends JpaRepository<ReportCategory, Integer> {
}
