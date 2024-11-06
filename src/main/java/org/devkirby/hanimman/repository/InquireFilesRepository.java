package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.InquireFiles;
import org.devkirby.hanimman.entity.Inquiries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquireFilesRepository extends JpaRepository<InquireFiles, Integer> {
    // 문의에 대해 삭제되지 않은 파일만 가져오기
    List<InquireFiles> findByParentAndDeletedAtIsNull(Inquiries parent);
}
