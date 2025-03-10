package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.CommonStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyAnnouncementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study Announcement V2", description = "스터디 공지 V2 API입니다.")
@RestController
@RequestMapping("/v2/study-announcements")
@RequiredArgsConstructor
public class StudyAnnouncementControllerV2 {

    private final CommonStudyServiceV2 commonStudyServiceV2;

    @Operation(summary = "스터디 공지 목록 조회", description = "스터디 공지 목록을 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyAnnouncementResponse> getStudyAnnouncements(@PathVariable Long studyId) {
        var response = commonStudyServiceV2.getStudyAnnouncements(studyId);
        return ResponseEntity.ok(response);
    }
}
