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

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    private LocalDateTime sessionStartAt;

    private String sessionTitle;

    private String sessionDescription;

    @Enumerated(EnumType.STRING)
    private Difficulty sessionDifficulty;

    // 스터디 휴강 여부
    private boolean isSessionCanceled;
}
