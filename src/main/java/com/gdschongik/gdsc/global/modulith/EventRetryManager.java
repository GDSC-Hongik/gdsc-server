package com.gdschongik.gdsc.global.modulith;

import com.gdschongik.gdsc.global.property.ModulithProperty;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.EventPublication;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRetryManager {

    private final ModulithProperty modulithProperty;
    private final IncompleteEventPublications incompletePublications;

    @Scheduled(fixedRateString = "${modulith.retry-interval}", timeUnit = TimeUnit.SECONDS)
    public void retryIncompleteEvents() {
        Instant now = Instant.now();
        Instant oldestAllowed = now.minusSeconds(modulithProperty.getMaxAge());
        Instant newestAllowed = now.minusSeconds(modulithProperty.getMinAge());

        incompletePublications.resubmitIncompletePublications(
                publication -> isWithinAgeRange(publication, oldestAllowed, newestAllowed));
    }

    private static boolean isWithinAgeRange(
            EventPublication publication, Instant oldestAllowed, Instant newestAllowed) {
        Instant publicationDate = publication.getPublicationDate();
        return publicationDate.isAfter(oldestAllowed) && publicationDate.isBefore(newestAllowed);
    }
}
