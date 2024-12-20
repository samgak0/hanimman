package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.TogetherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TogetherReportRepository extends JpaRepository<TogetherReport, Integer> {
}
