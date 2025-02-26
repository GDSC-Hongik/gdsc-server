package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.study.dto.request.StudyAnnouncementCreateUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.application.MentorStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyManagerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor Study V2", description = "스터디 V2 멘토 API입니다.")
@RestController
@RequestMapping("/v2/mentor/studies")
@RequiredArgsConstructor
public class MentorStudyControllerV2 {

    private final MentorStudyServiceV2 mentorStudyServiceV2;

    @Operation(summary = "내 스터디 조회", description = "내가 멘토로 있는 스터디를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<StudyManagerResponse>> getStudiesInCharge() {
        var response = mentorStudyServiceV2.getStudiesInCharge();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 정보 변경", description = "스터디 정보를 변경합니다.")
    @PutMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(@PathVariable Long studyId, @RequestBody StudyUpdateRequest request) {
        mentorStudyServiceV2.updateStudy(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 생성", description = "스터디의 공지사항을 생성합니다.")
    @PostMapping("/{studyId}/announcements")
    public ResponseEntity<Void> createStudyAnnouncement(
            @PathVariable Long studyId, @Valid @RequestBody StudyAnnouncementCreateUpdateRequest request) {
        mentorStudyServiceV2.createStudyAnnouncement(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 수정", description = "스터디의 공지사항을 수정합니다.")
    @PutMapping("/announcements/{studyAnnouncementId}")
    public ResponseEntity<Void> updateStudyAnnouncement(
            @PathVariable Long studyAnnouncementId, @Valid @RequestBody StudyAnnouncementCreateUpdateRequest request) {
        mentorStudyServiceV2.updateStudyAnnouncement(studyAnnouncementId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 삭제", description = "스터디의 공지사항을 삭제합니다.")
    @DeleteMapping("/announcements/{studyAnnouncementId}")
    public ResponseEntity<Void> deleteStudyAnnouncement(@PathVariable Long studyAnnouncementId) {
        mentorStudyServiceV2.deleteStudyAnnouncement(studyAnnouncementId);
        return ResponseEntity.ok().build();
    }
}
