package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudentMyCurrentStudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Study V2", description = "학생 스터디 V2 API입니다.")
@RestController
@RequestMapping("/v2/studies")
@RequiredArgsConstructor
public class StudentStudyControllerV2 {

    private final StudentStudyServiceV2 studentStudyServiceV2;

    @Operation(summary = "내 수강중인 스터디 조회", description = "나의 수강중인 스터디를 조회합니다.")
    @GetMapping("/me/ongoing")
    public ResponseEntity<StudentMyCurrentStudyResponse> getMyCurrentStudies() {
        StudentMyCurrentStudyResponse response = studentStudyServiceV2.getMyCurrentStudies();
        return ResponseEntity.ok(response);
    }
}
