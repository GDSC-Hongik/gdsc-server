package com.gdschongik.gdsc.domain.study.domain.vo;

import static com.gdschongik.gdsc.domain.study.domain.StudyStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.StudyStatus.CANCELLED;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.domain.Difficulty;
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

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Comment("과제 상태")
    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Assignment(
            String title, LocalDateTime deadline, String descriptionLink, Difficulty difficulty, StudyStatus status) {
        this.title = title;
        this.deadline = deadline;
        this.descriptionLink = descriptionLink;
        this.difficulty = difficulty;
        this.status = status;
    }

    public static Assignment createEmptyAssignment() {
        return Assignment.builder().status(NONE).build();
    }

    public static Assignment cancelAssignment() {
        return Assignment.builder().status(CANCELLED).build();
    }

    public static Assignment generateAssignment(String title, LocalDateTime deadline, String descriptionLink) {
        return Assignment.builder()
                .title(title)
                .deadline(deadline)
                .descriptionLink(descriptionLink)
                .status(StudyStatus.OPEN)
                .build();
    }

    public void validateSubmittable(LocalDateTime now) {
        if (status == NONE) {
            throw new CustomException(ASSIGNMENT_SUBMIT_NOT_PUBLISHED);
        }

        if (status == CANCELLED) {
            throw new CustomException(ASSIGNMENT_SUBMIT_CANCELLED);
        }

        if (now.isAfter(deadline)) {
            throw new CustomException(ASSIGNMENT_SUBMIT_DEADLINE_PASSED);
        }
    }

    // 데이터 전달 로직

    public boolean isCancelled() {
        return status == CANCELLED;
    }

    public boolean isDeadlineRemaining() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(deadline);
    }

    public boolean isDeadLineThisWeek() {
        LocalDate now = LocalDate.now(); // 현재 날짜
        LocalDate deadlineDate = deadline.toLocalDate(); // 마감일의 날짜 부분

        // 이번 주의 첫 번째 요일과 마지막 요일 가져오기 (월요일 ~ 일요일)
        LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);

        // 마감일이 이번 주 내에 있는지 확인
        return !deadlineDate.isBefore(startOfWeek) && !deadlineDate.isAfter(endOfWeek);
    }
}
