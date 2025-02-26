package com.gdschongik.gdsc.domain.studyv2.api;

import jakarta.validation.Valid;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.AttendanceCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Study V2", description = "사용자 스터디 V2 API입니다.")
@RestController
@RequestMapping("/v2/studies")
@RequiredArgsConstructor
public class StudentStudyControllerV2 {

    private final StudentStudyServiceV2 studentStudyServiceV2;

    @Operation(summary = "스터디 출석체크", description = "스터디에 출석체크합니다.")
    @PostMapping("/{studyId}/attend")
    public ResponseEntity<Void> attend(
            @PathVariable Long studyId,
            @RequestParam Long studySessionId,
            @RequestBody @Valid AttendanceCreateRequest request) {
        studentStudyServiceV2.attend(studyId, studySessionId, request);
        return ResponseEntity.ok().build();
    }
}
