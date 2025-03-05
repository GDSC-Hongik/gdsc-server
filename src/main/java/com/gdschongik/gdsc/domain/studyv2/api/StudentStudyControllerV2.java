package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyDashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Operation(summary = "스터디 수강신청", description = "모집중인 스터디에 수강신청 합니다. 여러 스터디에 수강신청 할 수 있습니다.")
    @PostMapping("/apply/{studyId}")
    public ResponseEntity<Void> applyStudy(@PathVariable Long studyId) {
        studentStudyServiceV2.applyStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 수강신청 취소", description = "수강신청을 취소합니다. 스터디 수강신청 기간 중에만 취소할 수 있습니다.")
    @DeleteMapping("/apply/{studyId}")
    public ResponseEntity<Void> cancelStudyApply(@PathVariable Long studyId) {
        studentStudyServiceV2.cancelStudyApply(studyId);
        return ResponseEntity.noContent().build();
    }
}
