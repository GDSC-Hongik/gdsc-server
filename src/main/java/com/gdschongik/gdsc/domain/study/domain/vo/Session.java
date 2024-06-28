package com.gdschongik.gdsc.domain.study.domain.vo;

import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    private LocalDateTime startAt;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Comment("스터디 휴강 여부")
    private boolean isCancelled;
}
