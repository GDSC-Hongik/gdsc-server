package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.MentorStudyService;
import com.gdschongik.gdsc.domain.study.dto.request.StudyNotificationRequest;
import com.gdschongik.gdsc.domain.study.dto.response.MentorStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor Study", description = "멘토 스터디 API입니다.")
@RestController
@RequestMapping("/mentor/studies")
@RequiredArgsConstructor
public class MentorStudyController {

    private final MentorStudyService mentorStudyService;

    @Operation(summary = "내 스터디 조회", description = "내가 멘토로 있는 스터디를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<MentorStudyResponse>> getStudiesInCharge() {
        List<MentorStudyResponse> response = mentorStudyService.getStudiesInCharge();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 공지 생성", description = "스터디의 공지사항을 생성합니다.")
    @PostMapping("/{studyId}/notifications")
    public ResponseEntity<Void> createStudyNotification(
            @PathVariable Long studyId, @Valid @RequestBody StudyNotificationRequest request) {
        mentorStudyService.createStudyNotification(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 수강생 명단 조회", description = "해당 스터디의 수강생 명단을 조회합니다")
    @GetMapping("/{studyId}/students")
    public ResponseEntity<List<StudyStudentResponse>> getStudyStudents(@PathVariable Long studyId) {
        List<StudyStudentResponse> response = mentorStudyService.getStudyStudents(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 공지 수정", description = "스터디의 공지사항을 수정합니다.")
    @PatchMapping("/notifications/{studyNotificationId}")
    public ResponseEntity<Void> updateStudyNotification(
            @PathVariable Long studyNotificationId, @Valid @RequestBody StudyNotificationRequest request) {
        mentorStudyService.updateStudyNotification(studyNotificationId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 삭제", description = "스터디의 공지사항을 삭제합니다.")
    @DeleteMapping("/notifications/{studyNotificationId}")
    public ResponseEntity<Void> deleteStudyNotification(@PathVariable Long studyNotificationId) {
        mentorStudyService.deleteStudyNotification(studyNotificationId);
        return ResponseEntity.ok().build();
    }
}
