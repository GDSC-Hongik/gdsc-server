package com.gdschongik.gdsc.domain.study.domain.vo;

import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalTime;
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
public class Session {

    private LocalTime startAt;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Comment("세션 상태")
    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Session(LocalTime startAt, String title, String description, Difficulty difficulty, StudyStatus status) {
        this.startAt = startAt;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.status = status;
    }

    public static Session createEmptySession() {
        return Session.builder().status(StudyStatus.NONE).build();
    }

    public static Session generateSession(
            LocalTime startAt, String title, String description, Difficulty difficulty, StudyStatus status) {
        return Session.builder()
                .startAt(startAt)
                .title(title)
                .description(description)
                .difficulty(difficulty)
                .status(status)
                .build();
    }

    // 데이터 전달 로직
    public boolean isOpen() {
        return status == StudyStatus.OPEN;
    }
}
