package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudyStudentResponse(
        @Schema(description = "멤버 아이디") Long memberId,
        @Schema(description = "학생 이름") String name,
        @Schema(description = "학번") String studentId,
        @Schema(description = "디스코드 사용자명") String discordUsername,
        @Schema(description = "디스코드 닉네임") String nickname,
        @Schema(description = "깃허브 링크") String githubLink) {
    public static StudyStudentResponse from(StudyHistory studyHistory) {
        return new StudyStudentResponse(
                studyHistory.getStudent().getId(),
                studyHistory.getStudent().getName(),
                studyHistory.getStudent().getStudentId(),
                studyHistory.getStudent().getDiscordUsername(),
                studyHistory.getStudent().getNickname(),
                studyHistory.getRepositoryLink());
    }
}
