package com.gdschongik.gdsc.domain.study.domain.vo;

import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import jakarta.persistence.Column;
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
public class Assignment {

    // 과제 마감 시각
    private LocalDateTime assignmentDueAt;

    private String assignmentTitle;

    @Column(columnDefinition = "TEXT")
    private String assignmentNotionLink;

    // 과제 휴강 여부
    private boolean isAssignmentCanceled;

    @Enumerated(EnumType.STRING)
    private Difficulty assignmentDifficulty;
}
