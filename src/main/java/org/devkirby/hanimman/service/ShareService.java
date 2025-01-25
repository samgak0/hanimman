package org.devkirby.hanimman.service;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ShareDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;

public interface ShareService {
    Integer create(ShareDTO shareDTO, String primaryAddressId) throws IOException;
    ShareDTO read(Integer id, CustomUserDetails loginUser);
    void update(ShareDTO shareDTO) throws IOException;
    void delete(Integer id);

    Page<ShareDTO> listAll(Pageable pageable, Boolean isEnd, String sortBy, String addressId, Integer userId);

    Page<ShareDTO> searchByKeywords
            (String keyword, Pageable pageable, Boolean isEnd, String sortBy, String addressId, Integer userId);

    void updateIsEnd();

    Page<ShareDTO> listByUserIdFavorite(Integer userId, Pageable pageable);

    File downloadImage(Integer id) throws IOException;

    File downloadProfileImage(Integer id) throws IOException;

    Page<ShareDTO> listByUserId(Integer userId, Pageable pageable);
}
