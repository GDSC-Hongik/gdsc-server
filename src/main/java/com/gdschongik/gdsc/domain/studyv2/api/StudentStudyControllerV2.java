package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyDashboardResponse;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyTodoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "수강중인 특정 스터디의 할 일 리스트 조회", description = "나의 수강중인 특정 스터디의 할 일 리스트를 조회합니다.")
    @GetMapping("/{studyId}/me/todo")
    public ResponseEntity<List<StudyTodoResponse>> getStudyTodoList(@PathVariable Long studyId) {
        var response = studentStudyServiceV2.getMyStudyTodos(studyId);
        return ResponseEntity.ok().body(response);
    }
}
