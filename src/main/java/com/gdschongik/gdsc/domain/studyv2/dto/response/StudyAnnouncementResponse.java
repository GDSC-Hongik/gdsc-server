package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyAnnouncementDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyCommonDto;

public record StudyAnnouncementResponse(StudyCommonDto study, StudyAnnouncementDto studyAnnouncement) {
    public static StudyAnnouncementResponse from(StudyAnnouncementV2 studyAnnouncement) {
        return new StudyAnnouncementResponse(
                StudyCommonDto.from(studyAnnouncement.getStudy()), StudyAnnouncementDto.from(studyAnnouncement));
    }
}
