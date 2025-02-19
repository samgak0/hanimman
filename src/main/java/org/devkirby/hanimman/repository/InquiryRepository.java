package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    List<Inquiry> findByTitleContaining(String keyword);
    
    @Query("SELECT i FROM Inquiry i WHERE i.title LIKE %:keyword% OR i.content LIKE %:keyword%")
    List<Inquiry> findByTitleOrContentContaining(String keyword);

    List<Inquiry> findByOrderByCreatedAtAsc();

    List<Inquiry> findByOrderByCreatedAtDesc();

    Page<Inquiry> findByIdAndDeletedAtIsNull(Integer id, Pageable pageable);
}
