package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Inquiry;
import org.devkirby.hanimman.entity.InquiryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryFileRepository extends JpaRepository<InquiryFile, Integer> {
    // 문의에 대해 삭제되지 않은 파일만 가져오기
    List<InquiryFile> findByParentAndDeletedAtIsNull(Inquiry parent);
}
