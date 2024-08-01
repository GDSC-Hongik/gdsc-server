package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudyMentorService;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @PutMapping("/assignment/{assignmentId}")
    public ResponseEntity<Void> createStudyAssignment(
            @PathVariable Long assignmentId, @Valid @RequestBody AssignmentCreateRequest request) {
        return null;
    }

    @Operation(summary = "스터디 과제 휴강 처리", description = "해당 주차 과제를 휴강 처리합니다.")
    @PatchMapping("/assignment/{studyDetailId}/cancel")
    public ResponseEntity<Void> cancelStudyAssignment(@PathVariable Long studyDetailId) {
        studyMentorService.cancelStudyAssignment(studyDetailId);
        return ResponseEntity.noContent().build();
    }
}
