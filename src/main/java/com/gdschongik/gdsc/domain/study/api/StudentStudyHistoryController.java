package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudentStudyHistoryService;
import com.gdschongik.gdsc.domain.study.dto.request.RepositoryUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentHistoryResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudentMyCompleteStudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Student Study History", description = "사용자 스터디 수강 이력 API입니다.")
@RestController
@RequestMapping("/study-history")
@RequiredArgsConstructor
public class StudentStudyHistoryController {

    private final StudentStudyHistoryService studentStudyHistoryService;

    @Operation(summary = "레포지토리 입력", description = "레포지토리를 입력합니다. 이미 제출한 과제가 있다면 수정할 수 없습니다.")
    @PutMapping("/{studyId}/repository")
    public ResponseEntity<Void> updateRepository(
            @PathVariable Long studyId, @Valid @RequestBody RepositoryUpdateRequest request) throws IOException {
        studentStudyHistoryService.updateRepository(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 과제 히스토리 목록 조회", description = "스터디 과제 제출 내역을 조회합니다.")
    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentHistoryResponse>> getAllAssignmentHistories(
            @RequestParam(name = "studyId") Long studyId) {
        List<AssignmentHistoryResponse> response = studentStudyHistoryService.getAllAssignmentHistories(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과제 제출하기", description = "과제를 제출합니다. 제출된 과제는 채점되어 제출내역에 반영됩니다.")
    @PostMapping("/submit")
    public ResponseEntity<Void> submitAssignment(@RequestParam(name = "studyDetailId") Long studyDetailId) {
        studentStudyHistoryService.submitAssignment(studyDetailId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 스터디 수료 내역 조회", description = "내가 수료한 스터디를 조회합니다.")
    @GetMapping("/me/complete")
    public ResponseEntity<List<StudentMyCompleteStudyResponse>> getMyCompleteStudy() {
        List<StudentMyCompleteStudyResponse> response = studentStudyHistoryService.getMyCompleteStudies();
        return ResponseEntity.ok(response);
    }
}
