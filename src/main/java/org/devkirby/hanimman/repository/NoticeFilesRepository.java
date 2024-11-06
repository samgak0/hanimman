package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.NoticeFiles;
import org.devkirby.hanimman.entity.Notices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeFilesRepository extends JpaRepository<NoticeFiles, Integer> {
    // 공지사항에 대해 삭제되지 않은 파일만 가져오기
    List<NoticeFiles> findByParentIdAndDeletedAtIsNull(Notices parent);
}
