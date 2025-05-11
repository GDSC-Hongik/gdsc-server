package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyAnnouncementServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyAnnouncementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study Announcement V2 - Student", description = "학생 스터디 공지 V2 API입니다.")
@RestController
@RequestMapping("/v2/study-announcements")
@RequiredArgsConstructor
public class StudentStudyAnnouncementControllerV2 {

    private final StudentStudyAnnouncementServiceV2 studentStudyAnnouncementServiceV2;

    @Operation(summary = "수강중인 특정 스터디의 공지 목록 조회", description = "나의 수강중인 특정 스터디의 공지 목록을 조회합니다.")
    @GetMapping("/{studyId}/me")
    public ResponseEntity<List<StudyAnnouncementResponse>> getStudyAnnouncements(@PathVariable Long studyId) {
        var response = studentStudyAnnouncementServiceV2.getStudyAnnouncements(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "수강중인 모든 스터디의 공지 목록 조회", description = "나의 수강중인 모든 스터디의 공지 목록을 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<List<StudyAnnouncementResponse>> getStudiesAnnouncements() {
        var response = studentStudyAnnouncementServiceV2.getStudiesAnnouncements();
        return ResponseEntity.ok(response);
    }
}
