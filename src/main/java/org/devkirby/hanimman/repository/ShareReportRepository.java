package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareReportRepository extends JpaRepository<ShareReport, Integer> {
}
