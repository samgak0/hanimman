package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherFavorite;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.TogetherFavoriteRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TogetherFavoriteServiceImpl implements TogetherFavoriteService {
    private final TogetherFavoriteRepository togetherFavoriteRepository;
    private final ModelMapper modelMapper;

    private TogetherRepository togetherRepository;
    private UserRepository userRepository;

    @Override
    @Transactional
    public void create(Integer userId, Integer parentId) {
        Together together = togetherRepository.findById(parentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if(togetherFavoriteRepository.existsByUserAndParent(user, together)) {
            throw new IllegalStateException("이미 즐겨찾기한 글입니다.");
        }
        TogetherFavorite togetherFavorite = TogetherFavorite.builder()
                .user(user)
                .parent(together)
                .build();
        togetherFavoriteRepository.save(togetherFavorite);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        togetherFavoriteRepository.deleteById(id);
    }

    @Override
    public int countByParentId(Integer parentId) {
        return togetherFavoriteRepository.countByParent(togetherRepository.findById(parentId).orElseThrow());
    }
}
