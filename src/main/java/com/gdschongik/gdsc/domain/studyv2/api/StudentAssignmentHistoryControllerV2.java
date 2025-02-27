package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentAssignmentHistoryServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Assignment History V2", description = "학생 과제 제출이력 API입니다.")
@RestController
@RequestMapping("/v2/assignment-histories")
@RequiredArgsConstructor
public class StudentAssignmentHistoryControllerV2 {

    private final StudentAssignmentHistoryServiceV2 studentAssignmentHistoryServiceV2;

    @Operation(summary = "내 과제 제출하기", description = "나의 과제를 제출합니다. 제출된 과제는 채점되어 제출내역에 반영됩니다.")
    @PostMapping("/submit")
    public ResponseEntity<Void> submitMyAssignment(@RequestParam(name = "studySessionId") Long studySessionId) {
        studentAssignmentHistoryServiceV2.submitMyAssignment(studySessionId);
        return ResponseEntity.ok().build();
    }
}
