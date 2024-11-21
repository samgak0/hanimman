package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareReviewDTO;
import org.devkirby.hanimman.entity.ShareReview;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ShareReviewRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareReviewServiceImpl implements ShareReviewService {
    private final ShareReviewRepository shareReviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createReview(ShareReviewDTO shareReviewDTO) {
        Integer userId = shareReviewDTO.getUserId();
        Integer targetId = shareReviewDTO.getTargetId();
        Integer parentId = shareReviewDTO.getParentId();

        if(shareReviewRepository.findByUserIdAndTargetIdAndParentIdAndDeletedAtIsNull(userId, targetId, parentId) != null) {
            throw new IllegalArgumentException("이미 해당 나눠요에 대한 후기를 작성하셨습니다.");
        } else {
            ShareReview shareReview = modelMapper.map(shareReviewDTO, ShareReview.class);
            shareReviewRepository.save(shareReview);
        }
    }

    @Override
    public ShareReviewDTO readReview(Integer id) {
        ShareReview shareReview = shareReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
        ShareReviewDTO shareReviewDTO = modelMapper.map(shareReview, ShareReviewDTO.class);
        return shareReviewDTO;
    }

    @Override
    @Transactional
    public void updateReview(ShareReviewDTO shareReviewDTO) {
        ShareReview shareReview = shareReviewRepository.findById(shareReviewDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
        modelMapper.map(shareReviewDTO, shareReview);
        shareReviewRepository.save(shareReview);
    }

    @Override
    @Transactional
    public void deleteReview(Integer id) {
        ShareReview shareReview = shareReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
        shareReview.setDeletedAt(Instant.now());
    }

    @Override
    public Page<ShareReviewDTO> getWrittenReviews(Integer userId, Pageable page) {
        return shareReviewRepository.findByUserIdAndDeletedAtIsNull(userId, page)
                .map(shareReview -> modelMapper.map(shareReview, ShareReviewDTO.class));
    }

    @Override
    public Page<ShareReviewDTO> getMyReviews(Integer targetId, Pageable page) {
        return shareReviewRepository.findByTargetIdAndDeletedAtIsNull(targetId, page)
                .map(shareReview -> modelMapper.map(shareReview, ShareReviewDTO.class));
    }

    private void setRating(Integer userId, Integer rating) {
        Optional<User> user = userRepository.findById(userId);
    }
}
