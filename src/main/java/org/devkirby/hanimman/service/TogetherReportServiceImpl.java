package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherReportDTO;
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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public void create(TogetherReportDTO togetherReportDTO) {
        Together together = togetherRepository.findById(togetherReportDTO.getTogetherId()).orElseThrow(()
                -> new RuntimeException("Together not found"));

        // Together의 deletedAt 컬럼이 null인지 확인
        if (together.getDeletedAt() != null) {
            throw new RuntimeException("삭제가 된 게시글입니다.");
        }

        User reportedUser = together.getUser(); // 신고당한 사람
        User reporterUser = userRepository.findById(togetherReportDTO.getReporterId()).orElseThrow(()
                -> new RuntimeException("Reporter not found"));
        ReportCategory category = reportCategoryRepository.findById(togetherReportDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

//        ZonedDateTime nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
//        Instant createdAt = nowKST.toInstant();

        togetherReportRepository.save(TogetherReport.builder()
                .reporter(reporterUser)
                .reported(reportedUser)
                .category(category)
                .parent(together)
                .createdAt(Instant.now())
                .build());
    }

    //삭제
    @Override
    @Transactional
    public void delete(Integer id) {
        togetherReportRepository.findById(id);
    }

    //조회
    @Override
    @Transactional
    public List<TogetherReport> findAllSelectReports() {
        List<TogetherReport> reports = togetherReportRepository.findAll()
                .stream()
                .filter(report -> report.getParent().getDeletedAt() != null) // 삭제된 데이터가 있는 Together만 조회
                .collect(Collectors.toList());

        // 삭제된 보고서의 개수를 출력
        System.out.println("Select Reports: " + reports.size());

        return reports;
    }

}
