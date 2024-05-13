package com.gdschongik.gdsc.domain.recruitment.domain.vo;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
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

    public static Period createPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return Period.builder().startDate(startDate).endDate(endDate).build();
    }

    private static void validatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new CustomException(ErrorCode.WRONG_DATE_ORDER);
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
