package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareReportsRepository extends JpaRepository<ShareReports, Integer> {
}
