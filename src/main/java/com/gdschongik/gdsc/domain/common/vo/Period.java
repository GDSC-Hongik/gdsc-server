package com.gdschongik.gdsc.domain.common.vo;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Period {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder(access = AccessLevel.PRIVATE)
    private Period(final LocalDateTime startDate, final LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return Period.builder().startDate(startDate).endDate(endDate).build();
    }

    private static void validatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new CustomException(DATE_PRECEDENCE_INVALID);
        }
    }

    public boolean isOpen() {
        // TODO: now를 내부에서 선언하지 않고 파라미터로 받아서 테스트 가능하도록 변경
        LocalDateTime now = LocalDateTime.now();
        return (now.isAfter(startDate) || now.isEqual(startDate)) && (now.isBefore(endDate) || now.isEqual(startDate));
    }

    public boolean test(int a) {
        return a > 5;
    }

    public void validatePeriodOverlap(LocalDateTime startDate, LocalDateTime endDate) {
        if (!this.endDate.isBefore(startDate) && !this.startDate.isAfter(endDate)) {
            throw new CustomException(PERIOD_OVERLAP);
        }
    }
}
