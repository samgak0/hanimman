package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShareService {
    void create(ShareDTO shareDTO, List<ShareImageDTO> shareImageDTOList);
    ShareDTO read(Integer id, User loginUser);
    void update(ShareDTO shareDTO, List<ShareImageDTO> shareImageDTOList);
    void delete(Integer id);

    Page<ShareDTO> listAll(Pageable pageable, Boolean isEnd);

    Page<ShareDTO> searchByKeywords(String keyword, Pageable pageable);

//    Page<ShareDTO> listNotEnd(Pageable pageable);
}
