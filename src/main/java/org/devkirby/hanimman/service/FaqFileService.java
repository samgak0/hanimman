package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.FaqFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FaqFileService {
    void create(FaqFileDTO faqFileDTO);
    FaqFileDTO read(Integer id);
    void update(FaqFileDTO faqFileDTO);
    void delete(Integer id);

    String uploadFile(MultipartFile file, Integer faqId) throws IOException;
    List<String> uploadFiles(List<MultipartFile> files, Integer faqId) throws IOException;
}
