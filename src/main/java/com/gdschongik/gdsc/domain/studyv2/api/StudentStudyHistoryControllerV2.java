package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.StudentStudyHistoryServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyHistoryRepositoryUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Study History V2", description = "학생 스터디 수강이력 API입니다.")
@RestController
@RequestMapping("/v2/study-histories")
@RequiredArgsConstructor
public class StudentStudyHistoryControllerV2 {

    private final StudentStudyHistoryServiceV2 studentStudyHistoryServiceV2;

    @Operation(summary = "내 레포지토리 입력", description = "나의 과제 제출 레포지토리를 입력합니다.")
    @PutMapping("/repositories/me")
    public ResponseEntity<Void> updateRepository(@Valid @RequestBody StudyHistoryRepositoryUpdateRequest request) {
        studentStudyHistoryServiceV2.updateRepository(request);
        return ResponseEntity.ok().build();
    }
}
