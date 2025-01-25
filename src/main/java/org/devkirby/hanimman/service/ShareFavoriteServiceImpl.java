package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareFavorite;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ShareFavoriteRepository;
import org.devkirby.hanimman.repository.ShareRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareFavoriteServiceImpl implements ShareFavoriteService {
    private final ShareFavoriteRepository shareFavoriteRepository;

    private final ShareRepository shareRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void create(Integer userId, Integer parentId) {
        Share share = shareRepository.findById(parentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if (shareFavoriteRepository.existsByUserAndParent(user, share)) {
            throw new IllegalStateException("이미 즐겨찾기한 글입니다.");
        }
        ShareFavorite shareFavorite = ShareFavorite.builder()
                .user(user)
                .parent(share)
                .build();
        shareFavoriteRepository.save(shareFavorite);
    }

    @Override
    @Transactional
    public void delete(Integer parentId, Integer userId) {
        Integer id = shareFavoriteRepository.findByUserAndParent(userRepository.findById(userId).orElseThrow(),
                shareRepository.findById(parentId).orElseThrow()).getId();
        shareFavoriteRepository.deleteById(id);
    }

    @Override
    public int countByParentId(Integer parentId) {
        return shareFavoriteRepository.countByParent(shareRepository.findById(parentId).orElseThrow());
    }
}
