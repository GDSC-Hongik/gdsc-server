package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record StudyAnnouncementDto(
        Long studyAnnouncementId,
        @Schema(description = "제목") String title,
        @Schema(description = "링크") String link,
        @Schema(description = "생성 일자") LocalDate createdDate) {

    public static StudyAnnouncementDto from(StudyAnnouncementV2 studyAnnouncement) {
        return new StudyAnnouncementDto(
                studyAnnouncement.getId(),
                studyAnnouncement.getTitle(),
                studyAnnouncement.getLink(),
                studyAnnouncement.getCreatedAt().toLocalDate());
    }
}
