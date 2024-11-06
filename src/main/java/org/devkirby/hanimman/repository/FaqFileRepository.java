package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqFileRepository extends JpaRepository<FaqFile, Integer> {
    // FAQ에 대해 삭제되지 않은 파일만 가져오기
    List<FaqFile> findByParentAndDeletedAtIsNull(Faq parent);
}
