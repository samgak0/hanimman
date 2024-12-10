package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ReportCategory;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReportCategoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

    @Test
    @DisplayName("신고 카테고리 등록 테스트")
    public void test1() {
        User manager = userRepository.findById(1).orElseThrow();

        ReportCategory reportCategory1 = ReportCategory.builder()
                .name("욕설/비방")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory1);

        ReportCategory reportCategory2 = ReportCategory.builder()
                .name("거래 금지 물품")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory1);

        ReportCategory reportCategory3 = ReportCategory.builder()
                .name("전문 판매업자")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory3);

        ReportCategory reportCategory4 = ReportCategory.builder()
                .name("거래 중 분쟁 발생")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory4);

        ReportCategory reportCategory5 = ReportCategory.builder()
                .name("사기")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory5);

        ReportCategory reportCategory6 = ReportCategory.builder()
                .name("중복 및 도배")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory6);

        ReportCategory reportCategory7 = ReportCategory.builder()
                .name("기타 부적절한 행위")
                .manager(manager)
                .build();
        reportCategoryRepository.save(reportCategory7);
    }
}
