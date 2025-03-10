package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudentStudyMyCurrentResponse;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyApplicableResponse;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyDashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Study V2", description = "학생 스터디 API입니다.")
@RestController
@RequestMapping("/v2/studies")
@RequiredArgsConstructor
public class StudentStudyControllerV2 {

    private final StudentStudyServiceV2 studentStudyServiceV2;

    @Operation(summary = "내 스터디 대시보드 조회", description = "나의 스터디 대시보드를 조회합니다.")
    @GetMapping("/{studyId}/me/dashboard")
    public ResponseEntity<StudyDashboardResponse> getMyStudyDashboard(@PathVariable Long studyId) {
        var response = studentStudyServiceV2.getMyStudyDashboard(studyId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "나의 신청 가능한 스터디 조회", description = "현재 모집 중인 스터디와 내가 신청한 스터디를 조회합니다.")
    @GetMapping("/applicable/me")
    public ResponseEntity<StudyApplicableResponse> getAllApplicableStudies() {
        var response = studentStudyServiceV2.getAllApplicableStudies();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 수강 중인 스터디 조회", description = "나의 이번 학기 수강 중인 스터디를 조회합니다.")
    @GetMapping("/me/ongoing")
    public ResponseEntity<StudentStudyMyCurrentResponse> getMyCurrentStudies() {
        var response = studentStudyServiceV2.getMyCurrentStudies();
        return ResponseEntity.ok(response);
    }
}
