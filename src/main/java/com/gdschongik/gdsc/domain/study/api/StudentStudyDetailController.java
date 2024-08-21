package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudentStudyDetailService;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentDashboardResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentSessionResponse;
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

    // TODO 스터디 세션 워딩을 커리큘럼으로 변경해야함
    @Operation(summary = "스터디 커리큘럼 조회", description = "해당 스터디의 커리큘럼들을 조회합니다.")
    @GetMapping("/sessions")
    public ResponseEntity<List<StudyStudentSessionResponse>> getStudySessions(
            @RequestParam(name = "studyId") Long studyId) {
        List<StudyStudentSessionResponse> response = studentStudyDetailService.getSessions(studyId);
        return ResponseEntity.ok(response);
    }
}
