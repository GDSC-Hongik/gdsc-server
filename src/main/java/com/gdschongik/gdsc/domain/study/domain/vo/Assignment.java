package com.gdschongik.gdsc.domain.study.domain.vo;

import static com.gdschongik.gdsc.domain.study.domain.StudyStatus.*;

import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
}
