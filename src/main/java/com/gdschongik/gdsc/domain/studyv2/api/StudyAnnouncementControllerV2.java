package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudyAnnouncementServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyAnnouncementDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

    private final StudyAnnouncementServiceV2 studyAnnouncementServiceV2;

    @Operation(summary = "스터디 공지 목록 조회", description = "스터디 공지 목록을 조회합니다. studyId가 없다면 수강중인 모든 스터디의 공지를 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<List<StudyAnnouncementDto>> getStudyAnnouncements(
            @PathVariable(required = false) Long studyId) {
        var response = studyAnnouncementServiceV2.getStudyAnnouncements(studyId);
        return ResponseEntity.ok(response);
    }
}
