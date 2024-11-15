package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ShareImageService {
    void create (ShareImageDTO shareImageDTO);
    ShareImageDTO read(Integer id);
    void update (ShareImageDTO shareImageDTO);
    void delete (Integer id);
    void deleteByParent(Integer shareId);

    String uploadImage(MultipartFile file, Integer shareId) throws IOException;
    List<String> uploadImages(List<MultipartFile> files, Integer shareId) throws IOException;
}
