package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Study Mentor Management", description = "스터디 멘토 관리 API입니다.")
@RestController
@RequestMapping("/admin/mentor/management")
@RequiredArgsConstructor
public class StudyMentorController {

    @Operation(summary = "스터디 과제 개설", description = "멘토나 어드민이 과제를 개설할 수 있습니다.")
    @PostMapping
    public ResponseEntity<Void> createStudyAssignment(
            @PathVariable Long assignmentId, @Valid @RequestBody AssignmentCreateRequest request) {
        return null;
    }
}
