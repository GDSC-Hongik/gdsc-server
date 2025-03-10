package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyAnnouncementDto;
import java.util.List;

public record StudyAnnouncementResponse(List<StudyAnnouncementDto> studyAnnouncements) {
    public static StudyAnnouncementResponse from(List<StudyAnnouncementV2> studyAnnouncements) {
        return new StudyAnnouncementResponse(
                studyAnnouncements.stream().map(StudyAnnouncementDto::from).toList());
    }
}
