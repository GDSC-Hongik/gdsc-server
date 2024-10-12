package com.gdschongik.gdsc.domain.common.vo;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
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
        LocalDateTime now = LocalDateTime.now();
        return (now.isAfter(startDate) || now.isEqual(startDate)) && (now.isBefore(endDate) || now.isEqual(startDate));
    }

    public void validatePeriodOverlap(LocalDateTime startDate, LocalDateTime endDate) {
        if (!this.endDate.isBefore(startDate) && !this.startDate.isAfter(endDate)) {
            throw new CustomException(PERIOD_OVERLAP);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period that = (Period) o;
        return startDate == that.startDate && endDate == that.endDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
