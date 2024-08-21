package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyAnnouncement;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record StudyAnnouncementResponse(
        Long studyAnnounceId,
        @Schema(description = "제목") String title,
        @Schema(description = "링크") String link,
        @Schema(description = "생성 일자") LocalDate createdDate) {

    public static StudyAnnouncementResponse from(StudyAnnouncement studyAnnouncement) {
        return new StudyAnnouncementResponse(
                studyAnnouncement.getId(),
                studyAnnouncement.getTitle(),
                studyAnnouncement.getLink(),
                studyAnnouncement.getCreatedAt().toLocalDate());
    }
}
