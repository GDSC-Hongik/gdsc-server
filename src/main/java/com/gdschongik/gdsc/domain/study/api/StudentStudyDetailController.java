package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudentStudyDetailService;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentDashboardResponse;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Study Detail", description = "수강자 스터디 상세 API입니다.")
@RestController
@RequestMapping("/study-details")
@RequiredArgsConstructor
public class StudentStudyDetailController {

    private final StudentStudyDetailService studentStudyDetailService;

    @Operation(summary = "내 제출 가능한 과제 조회", description = "나의 제출 가능한 과제를 조회합니다.")
    @GetMapping("/assignments/dashboard")
    public ResponseEntity<AssignmentDashboardResponse> getSubmittableAssignments(
            @RequestParam(name = "studyId") Long studyId) {
        AssignmentDashboardResponse response = studentStudyDetailService.getSubmittableAssignments(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이번주 제출해야 할 과제 조회", description = "마감 기한이 이번주까지인 과제를 조회합니다.")
    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentStatusResponse>> getAssignmentsToSubmit(
            @RequestParam(name = "studyId") Long studyId) {
        List<AssignmentStatusResponse> response = studentStudyDetailService.getAssignmentsToSubmit(studyId);
        return ResponseEntity.ok(response);
    }
}
