package com.gdschongik.gdsc.domain.study.domain.vo;

import static com.gdschongik.gdsc.domain.study.domain.StudyStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {

    private String title;

    @Comment("과제 마감 시각")
    private LocalDateTime deadline;

    @Column(columnDefinition = "TEXT")
    private String descriptionLink;

    @Comment("과제 상태")
    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Assignment(String title, LocalDateTime deadline, String descriptionLink, StudyStatus status) {
        this.title = title;
        this.deadline = deadline;
        this.descriptionLink = descriptionLink;
        this.status = status;
    }

    public static Assignment of(String title, LocalDateTime deadline, String descriptionLink) {
        return Assignment.builder()
                .title(title)
                .deadline(deadline)
                .descriptionLink(descriptionLink)
                .status(StudyStatus.OPEN)
                .build();
    }

    public static Assignment empty() {
        return Assignment.builder().status(NONE).build();
    }

    public static Assignment canceled() {
        return Assignment.builder().status(CANCELED).build();
    }

    public void validateSubmittable(LocalDateTime now) {
        if (status == NONE) {
            throw new CustomException(ASSIGNMENT_SUBMIT_NOT_PUBLISHED);
        }

        if (status == CANCELED) {
            throw new CustomException(ASSIGNMENT_SUBMIT_CANCELED);
        }

        if (now.isAfter(deadline)) {
            throw new CustomException(ASSIGNMENT_SUBMIT_DEADLINE_PASSED);
        }
    }

    // 데이터 전달 로직

    public boolean isOpen() {
        return status == OPEN;
    }

    public boolean isCanceled() {
        return status == CANCELED;
    }

    public boolean isNone() {
        return status == NONE;
    }

    public boolean isDeadlineRemaining() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(deadline);
    }

    public boolean isDeadLineThisWeek() {
        // 현재 날짜와 마감일의 날짜 부분을 비교할 것이므로 LocalDate로 변환
        // TODO: now를 내부에서 선언하지 않고 파라미터로 받아서 테스트 가능하도록 변경
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(DayOfWeek.MONDAY); // 이번 주 월요일
        LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY); // 이번 주 일요일

        // 마감일의 날짜 부분을 가져옴
        LocalDate deadlineDate = deadline.toLocalDate();

        // 마감일이 이번 주 내에 있는지 확인
        return !deadlineDate.isBefore(startOfWeek) && !deadlineDate.isAfter(endOfWeek);
    }
}
