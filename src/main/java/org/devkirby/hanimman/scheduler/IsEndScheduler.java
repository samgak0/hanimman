package org.devkirby.hanimman.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.devkirby.hanimman.service.ShareService;
import org.devkirby.hanimman.service.TogetherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IsEndScheduler {
    private final ShareService shareService;
    private final TogetherService togetherService;

    // 매 0분, 30분 마다 실행
    @Scheduled(cron = "10 0,30 * * * *")
    public void updateIsEnd() {
        log.info("updateIsEnd() 실행");
        shareService.updateIsEnd();
        togetherService.updateIsEnd();
    }

}
