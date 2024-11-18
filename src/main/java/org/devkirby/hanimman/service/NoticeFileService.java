package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.NoticeFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoticeFileService {
    void create(NoticeFileDTO noticeFileDTO);
    NoticeFileDTO read(Integer id);
    void update(NoticeFileDTO noticeFileDTO);
    void delete(Integer id);
    void deleteByParent(Integer noticeId);

    String uploadFile(MultipartFile file, Integer noticeId) throws IOException;
    List<String> uploadFiles(List<MultipartFile> files, Integer noticeId) throws IOException;


}
