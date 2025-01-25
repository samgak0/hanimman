package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.ShareReportDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareReportServiceImpl implements ShareReportService {
    private final ShareReportRepository shareReportRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ShareRepository shareRepository;
    private final ReportCategoryRepository reportCategoryRepository;

    @Override
    @Transactional
    public void create(ShareReportDTO shareReportDTO) {
        Share share = shareRepository.findById(shareReportDTO.getShareId()).orElseThrow(()
                -> new RuntimeException("Share not found"));
        if(share.getDeletedAt() != null) {
            throw new RuntimeException("삭제된 게시글입니다.");
        }

        User reportedUser = share.getUser();
        User reporterUser = userRepository.findById(shareReportDTO.getReportedId()).orElseThrow(()
                -> new RuntimeException("Reporter not found"));
        ReportCategory category = reportCategoryRepository.findById(shareReportDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        shareReportRepository.save(ShareReport.builder()
                .reporter(reporterUser)
                .reported(reportedUser)
                .category(category)
                .parent(share)
                .build());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        shareReportRepository.deleteById(id);
    }

    @Override
    public List<ReportCategoryDTO> findAllCategories() {
        return reportCategoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, ReportCategoryDTO.class))
                .collect(Collectors.toList());
    }
}
