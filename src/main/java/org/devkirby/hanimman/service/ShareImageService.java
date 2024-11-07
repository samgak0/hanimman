package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ShareImageService {
    void create (ShareImageDTO shareImageDTO);
    ShareImageDTO read(Integer id);
    void update (ShareImageDTO shareImageDTO);
    void delete (Integer id);

    String uploadImage(MultipartFile file, Integer shareId) throws IOException;
}
