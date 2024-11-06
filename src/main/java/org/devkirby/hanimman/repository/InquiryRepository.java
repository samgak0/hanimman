package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    List<Inquiry> findByTitleContaining(String keyword);

    List<Inquiry> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    List<Inquiry> findByOrderByCreatedAtAsc();

    List<Inquiry> findByOrderByCreatedAtDesc();
}
