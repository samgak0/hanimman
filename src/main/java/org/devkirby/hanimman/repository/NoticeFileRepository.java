package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.NoticeFile;
import org.devkirby.hanimman.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeFileRepository extends JpaRepository<NoticeFile, Integer> {
    // 공지사항에 대해 삭제되지 않은 파일만 가져오기
    List<NoticeFile> findByParentAndDeletedAtIsNull(Notice parent);

    List<NoticeFile> findAllByParentId(Integer noticeId);
}
