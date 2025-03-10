package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface InquiryFileService {
    void create(InquiryFileDTO inquiryFileDTO);
    InquiryFileDTO read(Integer id);
    void update(InquiryFileDTO inquiryFileDTO);
    void delete(Integer id);

    String uploadFile(MultipartFile file, Integer inquiryId) throws IOException;
    List<String> uploadFiles(List<MultipartFile> files, Integer inquiryId) throws IOException;
}
