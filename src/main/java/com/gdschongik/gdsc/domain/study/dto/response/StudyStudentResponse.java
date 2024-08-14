package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudyStudentResponse(
        Long memberId,
        String name,
        String studentId,
        String githubLink,
        String discordName,
        String discordUsername
) {
    public static StudyStudentResponse from(StudyHistory studyHistory) {
       return null;
    }
}
