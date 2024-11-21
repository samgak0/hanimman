package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherReviewDTO;
import org.devkirby.hanimman.entity.TogetherReview;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.TogetherReviewRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TogetherReviewServiceImpl implements TogetherReviewService {
    private final TogetherReviewRepository togetherReviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createReview(TogetherReviewDTO togetherReviewDTO) {
        Integer userId = togetherReviewDTO.getUserId();
        Integer targetId = togetherReviewDTO.getTargetId();
        Integer parentId = togetherReviewDTO.getParentId();

        Optional<TogetherReview> existingReview = togetherReviewRepository.findByUserIdAndTargetIdAndParentIdAndDeletedAtIsNull(userId, targetId, parentId);
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("이미 해당 함께요에 대한 후기를 작성하셨습니다.");
        }
        setRating(targetId, togetherReviewDTO.getRating());
        TogetherReview togetherReview = modelMapper.map(togetherReviewDTO, TogetherReview.class);
        togetherReviewRepository.save(togetherReview);
    }

    @Override
    public TogetherReviewDTO readReview(Integer id) {
        TogetherReview togetherReview = togetherReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
        TogetherReviewDTO togetherReviewDTO = modelMapper.map(togetherReview, TogetherReviewDTO.class);
        return togetherReviewDTO;
    }

    @Override
    @Transactional
    public void updateReview(TogetherReviewDTO togetherReviewDTO) {
        TogetherReview togetherReview = togetherReviewRepository.findById(togetherReviewDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
        Optional<User> user = userRepository.findById(togetherReview.getTarget().getId());
        Integer brix = user.get().getBrix() - togetherReview.getRating() + togetherReviewDTO.getRating();
        setRating(togetherReviewDTO.getTargetId(), brix);
        modelMapper.map(togetherReviewDTO, togetherReview);
        togetherReviewRepository.save(togetherReview);
    }

    @Override
    @Transactional
    public void deleteReview(Integer id) {
        TogetherReview togetherReview = togetherReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
        togetherReview.setDeletedAt(Instant.now());
        togetherReviewRepository.save(togetherReview);
    }

    @Override
    public Page<TogetherReviewDTO> getWrittenReviews(Integer userId, Pageable page) {
        return togetherReviewRepository.findByUserIdAndDeletedAtIsNull(userId, page)
                .map(togetherReview -> modelMapper.map(togetherReview, TogetherReviewDTO.class));
    }

    @Override
    public Page<TogetherReviewDTO> getAcceptReviews(Integer targetId, Pageable page) {
        return togetherReviewRepository.findByTargetIdAndDeletedAtIsNull(targetId, page)
                .map(togetherReview -> modelMapper.map(togetherReview, TogetherReviewDTO.class));
    }

    private void setRating(Integer targetId, Integer rating) {
        Optional<User> user = userRepository.findById(targetId);
        if(user.isEmpty()){
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
        }
        Integer brix = user.get().getBrix() + rating;
        user.get().setBrix(brix);
        userRepository.save(user.get());
    }
}
