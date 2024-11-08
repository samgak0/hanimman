package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.entity.ReportCategory;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareReport;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ReportCategoryRepository;
import org.devkirby.hanimman.repository.ShareReportRepository;
import org.devkirby.hanimman.repository.ShareRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareReportServiceImpl implements ShareReportService {
    private final ShareReportRepository shareReportRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ShareRepository shareRepository;
    private final ReportCategoryRepository reportCategoryRepository;

    @Override
    public void create(Integer reporterId, Integer categoryId, Integer parentId) {
        Share share = shareRepository.findById(parentId).orElseThrow();
        User reportedUser = share.getUser();
        User reporterUser = userRepository.findById(reporterId).orElseThrow();
        ReportCategory category = reportCategoryRepository.findById(categoryId).orElseThrow();
        shareReportRepository.save(ShareReport.builder()
                .reporter(reporterUser)
                .reported(reportedUser)
                .category(category)
                .parent(share)
                .build());
    }

    @Override
    public void delete(Integer id) {
        shareReportRepository.deleteById(id);
    }
}
