package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudyHistoryManagerDto(
        @Schema(description = "멤버 아이디") Long memberId,
        @Schema(description = "학생 이름") String name,
        @Schema(description = "학번") String studentId,
        @Schema(description = "디스코드 사용자명") String discordUsername,
        @Schema(description = "디스코드 닉네임") String nickname,
        @Schema(description = "깃허브 링크") String githubLink,
        @Schema(description = "수료 상태") StudyHistoryStatus studyHistoryStatus) {
    public static StudyHistoryManagerDto from(StudyHistoryV2 studyHistory) {
        return new StudyHistoryManagerDto(
                studyHistory.getStudent().getId(),
                studyHistory.getStudent().getName(),
                studyHistory.getStudent().getStudentId(),
                studyHistory.getStudent().getDiscordUsername(),
                studyHistory.getStudent().getNickname(),
                studyHistory.getRepositoryLink(),
                studyHistory.getStatus());
    }
}
