package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.study.dto.request.StudyAnnouncementCreateUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.application.MentorStudyAnnouncementServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyAnnouncementCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor Study Announcement V2", description = "스터디 공지 V2 멘토 API입니다.")
@RestController
@RequestMapping("/v2/mentor/study-announcements")
@RequiredArgsConstructor
public class MentorStudyAnnouncementControllerV2 {

    private final MentorStudyAnnouncementServiceV2 mentorStudyAnnouncementServiceV2;

    @Operation(summary = "스터디 공지 생성", description = "스터디의 공지사항을 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createStudyAnnouncement(@Valid @RequestBody StudyAnnouncementCreateRequest request) {
        mentorStudyAnnouncementServiceV2.createStudyAnnouncement(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 수정", description = "스터디의 공지사항을 수정합니다.")
    @PutMapping("/{studyAnnouncementId}")
    public ResponseEntity<Void> updateStudyAnnouncement(
            @PathVariable Long studyAnnouncementId, @Valid @RequestBody StudyAnnouncementCreateUpdateRequest request) {
        mentorStudyAnnouncementServiceV2.updateStudyAnnouncement(studyAnnouncementId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 삭제", description = "스터디의 공지사항을 삭제합니다.")
    @DeleteMapping("/{studyAnnouncementId}")
    public ResponseEntity<Void> deleteStudyAnnouncement(@PathVariable Long studyAnnouncementId) {
        mentorStudyAnnouncementServiceV2.deleteStudyAnnouncement(studyAnnouncementId);
        return ResponseEntity.ok().build();
    }
}
