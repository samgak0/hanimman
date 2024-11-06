package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TogetherReportsRepository extends JpaRepository<ShareReports, Integer> {
}
