package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqFilesRepository extends JpaRepository<FaqFiles, Integer> {
    // FAQ에 대해 삭제되지 않은 파일만 가져오기
    List<FaqFiles> findByParentAndDeletedAtIsNull(Faq parent);
}
