package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqFileRepository extends JpaRepository<FaqFile, Integer> {
    List<FaqFile> findByParent(Faq parent);

    void deleteByParent(Faq existingFaq);
}
