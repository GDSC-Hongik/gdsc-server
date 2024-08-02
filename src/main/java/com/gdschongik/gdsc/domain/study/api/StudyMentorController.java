package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudyMentorService;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mentor Study", description = "멘토 스터디 관리 API입니다.")
@RestController
@RequestMapping("/mentor/studies")
@RequiredArgsConstructor
public class StudyMentorController {

    private final StudyMentorService studyMentorService;

    @Operation(summary = "스터디 과제 개설", description = "멘토만 과제를 개설할 수 있습니다.")
    @PatchMapping("/assignment/{studyDetailId}")
    public ResponseEntity<Void> createStudyAssignment(
            @PathVariable Long studyDetailId, @Valid @RequestBody AssignmentCreateRequest request) {
        studyMentorService.createStudyAssignment(studyDetailId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 주차별 과제 목록 조회", description = "주차별 스터디 과제 목록을 조회합니다.")
    @GetMapping("/assignments/{studyId}")
    public ResponseEntity<List<AssignmentResponse>> getWeeklyAssignments(@PathVariable Long studyId) {
        List<AssignmentResponse> response = studyMentorService.getWeeklyAssignments(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 과제 상세 조회", description = "멘토가 자신의 스터디 과제를 조회합니다.")
    @GetMapping("/assignments/{studyDetailId}")
    public ResponseEntity<AssignmentResponse> getStudyAssignment(@PathVariable Long studyDetailId) {
        AssignmentResponse response = studyMentorService.getAssignment(studyDetailId);
        return ResponseEntity.ok(response);
    }
}
