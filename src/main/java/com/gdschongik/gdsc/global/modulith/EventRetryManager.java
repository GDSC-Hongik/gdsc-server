package com.gdschongik.gdsc.global.modulith;

import com.gdschongik.gdsc.global.property.ModulithProperty;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.EventPublication;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>스프링 모듈리스 이벤트 재시도 관리자입니다.</p>
 *
 * <p>짧은 시간(최대 10초) 내 처리되는 API 요청 등의 작업을 상정하고 있습니다.</p>
 *
 * <p>정확히 한 번(exactly-once) 실행되어야 하는 장기간 비동기 실행 작업은 재시도되지 않도록,
 * <code>@TransactionalEventHandler()</code> 에 의해 트리거되지 않게 주의해야 합니다.</p>
 *
 * <p>일반 재시도: 최소 재시도 시간(기본 10초) ~ 최대 재시도 시간(기본 30초) 사이의 이벤트를
 * 재시도 간격(기본 5초)으로 재시도합니다.</p>
 *
 * <p>DLQ 처리: 최대 재시도 시간(기본 30초) 이후에도 처리되지 않은 데드 레터 이벤트에 대한 처리를 수행합니다.
 * 별도 완료 처리 혹은 DLQ 이동은 불가능합니다.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventRetryManager {

    private final ModulithProperty modulithProperty;
    private final IncompleteEventPublications incompletePublications;

    @Scheduled(fixedRateString = "${modulith.retry-interval-second}", timeUnit = TimeUnit.SECONDS)
    public void retryIncompleteEvents() {
        Instant now = Instant.now();
        Instant oldestAllowed = now.minusSeconds(modulithProperty.getMaxAge());
        Instant newestAllowed = now.minusSeconds(modulithProperty.getMinAge());

        incompletePublications.resubmitIncompletePublications(
                publication -> isWithinAgeRange(publication, oldestAllowed, newestAllowed));
    }

    private static boolean isWithinAgeRange(
            EventPublication publication, Instant oldestAllowed, Instant newestAllowed) {
        log.info("[EventRetryManager] 이벤트 재시도: uuid={}", publication.getIdentifier());
        Instant publicationDate = publication.getPublicationDate();
        return publicationDate.isAfter(oldestAllowed) && publicationDate.isBefore(newestAllowed);
    }

    @Scheduled(fixedRateString = "${modulith.dlq-interval-minute}", timeUnit = TimeUnit.MINUTES)
    public void logDeadLetterEvents() {
        List<UUID> deadLetterIds = getDeadLetterIds();

        if (!deadLetterIds.isEmpty()) {
            log.warn("[EventRetryManager] 데드 레터 발생: ids={}", deadLetterIds);
        }
    }

    private List<UUID> getDeadLetterIds() {
        Instant now = Instant.now();
        Instant oldestAllowed = now.minusSeconds(modulithProperty.getMaxAge());
        List<UUID> deadLetterIds = new ArrayList<>();

        incompletePublications.resubmitIncompletePublications(
                publication -> captureDeadLetterAndAlwaysReturnFalse(publication, deadLetterIds, oldestAllowed));

        return deadLetterIds;
    }

    /**
     * 데드 레터 이벤트를 캡처하고 ID를 목록에 추가합니다.
     * 항상 false를 반환하여 재시도를 수행하지 않습니다.
     */
    private static boolean captureDeadLetterAndAlwaysReturnFalse(
            EventPublication publication, List<UUID> deadLetterIds, Instant oldestAllowed) {
        if (isDeadLetter(publication, oldestAllowed)) {
            deadLetterIds.add(publication.getIdentifier());
        }
        return false;
    }

    private static boolean isDeadLetter(EventPublication publication, Instant oldestAllowed) {
        return publication.getPublicationDate().isBefore(oldestAllowed);
    }
}
