package com.gdschongik.gdsc.global.modulith;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.events.EventPublication;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring Modulith 이벤트 재시도 관리자입니다.
 * HTTP API 기반 애플리케이션에 최적화된 이벤트 재시도 및 실패 처리 전략을 구현합니다.
 * - 일반 재시도: 최소 처리 시간(30초) ~ 최대 재시도 시간(1시간) 사이의 이벤트
 * - DLQ 처리: 최대 재시도 시간(1시간)을 초과한 이벤트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventRetryManager {

    private final IncompleteEventPublications incompletePublications;

    // 설정값 - 프로퍼티로 외부화 가능
    @Value("${event.retry.min-age-seconds:30}")
    private int minAgeSeconds;

    @Value("${event.retry.max-age-minutes:60}")
    private int maxAgeMinutes;

    @Value("${event.retry.interval-seconds:120}")
    private int retryIntervalSeconds;

    @Value("${event.dlq.interval-minutes:60}")
    private int dlqIntervalMinutes;

    /**
     * 정규 재시도 스케줄러
     * MIN_AGE ~ MAX_AGE 사이의 이벤트만 재시도
     */
    @Scheduled(fixedRateString = "${event.retry.interval-ms:120000}")
    public void retryIncompleteEvents() {
        Duration minAge = Duration.ofSeconds(minAgeSeconds);
        Duration maxAge = Duration.ofMinutes(maxAgeMinutes);

        log.debug("Starting normal retry cycle for events between {} and {}", minAge, maxAge);

        try {
            incompletePublications.resubmitIncompletePublications(publication -> {
                Duration age = Duration.between(publication.getPublicationDate(), Instant.now());

                // 최소~최대 시간 범위 내의 이벤트만 재시도
                boolean shouldRetry = age.compareTo(minAge) >= 0 && age.compareTo(maxAge) < 0;
                if (shouldRetry) {
                    log.debug(
                            "Retrying event: id={}, type={}, age={}",
                            publication.getIdentifier(),
                            publication.getEvent().getClass().getSimpleName(),
                            age);
                }
                return shouldRetry;
            });
        } catch (Exception e) {
            log.error("Error during event retry processing", e);
        }
    }

    /**
     * 장기 실패 이벤트 처리 스케줄러 (DLQ)
     * MAX_AGE를 초과한 이벤트를 검출하여 DLQ 처리
     */
    @Scheduled(fixedRateString = "${event.dlq.interval-ms:3600000}")
    public void handleDeadLetterEvents() {
        Duration maxAge = Duration.ofMinutes(maxAgeMinutes);

        log.debug("Starting dead letter queue processing for events older than {}", maxAge);
        List<EventPublication> deadLetterEvents = new ArrayList<>();

        try {
            // MAX_AGE를 초과한 이벤트 탐색 (재시도는 하지 않음)
            incompletePublications.resubmitIncompletePublications(publication -> {
                Duration age = Duration.between(publication.getPublicationDate(), Instant.now());

                if (age.compareTo(maxAge) >= 0) {
                    // 이벤트를 수집만 하고 재시도하지 않음
                    deadLetterEvents.add(publication);
                }
                // 중요: false를 반환하여 어떤 이벤트도 재시도하지 않음
                return false;
            });

            // 수집된 장기 실패 이벤트 처리
            for (EventPublication failedEvent : deadLetterEvents) {
                processDeadLetterEvent(failedEvent);
            }

            if (!deadLetterEvents.isEmpty()) {
                log.info("Dead letter processing completed: {} events processed", deadLetterEvents.size());
            }
        } catch (Exception e) {
            log.error("Error during dead letter event processing", e);
        }
    }

    /**
     * 개별 DLQ 이벤트 처리
     * 이 메서드는 MAX_AGE를 초과한 이벤트에 대한 처리 로직을 구현합니다.
     */
    private void processDeadLetterEvent(EventPublication publication) {
        Duration age = Duration.between(publication.getPublicationDate(), Instant.now());

        // 1. 상세 로깅
        log.error(
                "Permanently failed event: id={}, type={}, age={}, published={}",
                publication.getIdentifier(),
                publication.getEvent().getClass().getSimpleName(),
                age,
                publication.getPublicationDate());

        try {
            // 2. 이벤트 데이터 로깅 (이벤트 내용을 분석하기 위함)
            log.error("Failed event data: {}", publication.getEvent().toString());

            // 3. DLQ 저장 로직 구현 (필요에 따라)
            // saveToDLQ(publication);

            // 4. 알림 전송 (선택 사항)
            // alertService.sendAlert("Permanent event failure", publication);
        } catch (Exception e) {
            log.error("Error processing dead letter event: {}", publication.getIdentifier(), e);
        }
    }
}
