package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudyService;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentHistoryResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study", description = "사용자 스터디 API입니다.")
@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @Operation(summary = "스터디 과제 히스토리 목록 조회", description = "스터디 과제 제출 내역을 조회합니다.")
    @GetMapping("/{studyId}/assignments")
    public ResponseEntity<List<AssignmentHistoryResponse>> getAllAssignmentHistories(@PathVariable Long studyId) {
        List<AssignmentHistoryResponse> response = studyService.getAllAssignmentHistories(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신청 가능한 스터디 조회", description = "모집 기간 중에 있는 스터디를 조회합니다.")
    @GetMapping("/apply")
    public ResponseEntity<List<StudyResponse>> getAllApplicableStudies() {
        List<StudyResponse> response = studyService.getAllApplicableStudies();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "스터디 수강신청", description = "스터디에 수강신청 합니다. 모집 기간 중이어야 하고, 이미 수강 중인 스터디가 없어야 합니다.")
    @PostMapping("/apply/{studyId}")
    public ResponseEntity<Void> applyStudy(@PathVariable Long studyId) {
        studyService.applyStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 수강신청 취소", description = "수강신청을 취소합니다. 스터디 수강신청 기간 중에만 취소할 수 있습니다.")
    @DeleteMapping("/apply/{studyId}")
    public ResponseEntity<Void> cancelStudyApply(@PathVariable Long studyId) {
        studyService.cancelStudyApply(studyId);
        return ResponseEntity.noContent().build();
    }
}
