package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.entity.ReportCategory;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherReport;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ReportCategoryRepository;
import org.devkirby.hanimman.repository.TogetherReportRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TogetherReportServiceImpl implements TogetherReportService {
    private final TogetherReportRepository togetherReportRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TogetherRepository togetherRepository;
    private final ReportCategoryRepository reportCategoryRepository;

    @Override
    @Transactional
    public void create(Integer reporterId, Integer categoryId, Integer parentId) {
        Together together = togetherRepository.findById(parentId).orElseThrow();
        User reportedUser = together.getUser();
        User reporterUser = userRepository.findById(reporterId).orElseThrow();
        ReportCategory category = reportCategoryRepository.findById(categoryId).orElseThrow();
        togetherReportRepository.save(TogetherReport.builder()
                .reporter(reporterUser)
                .reported(reportedUser)
                .category(category)
                .parent(together)
                .build());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        togetherReportRepository.deleteById(id);
    }
}
