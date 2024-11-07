package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TogetherImageService {
    void create(TogetherImageDTO togetherImageDTO);
    TogetherImageDTO read(Integer id);
    void update(TogetherImageDTO togetherImageDTO);
    void delete(Integer id);

    String uploadImage(MultipartFile file, Integer togetherId) throws IOException;
}
