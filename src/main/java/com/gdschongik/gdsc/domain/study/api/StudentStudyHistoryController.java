package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudentStudyHistoryService;
import com.gdschongik.gdsc.domain.study.dto.request.RepositoryUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student Study History", description = "사용자 스터디 수강 이력 API입니다.")
@RestController
@RequestMapping("/study-history")
@RequiredArgsConstructor
public class StudentStudyHistoryController {

    private final StudentStudyHistoryService studentStudyHistoryService;

    @Operation(summary = "레포지토리 입력", description = "레포지토리를 입력합니다. 이미 제출한 과제가 있다면 수정할 수 없습니다.")
    @PutMapping("/{studyHistoryId}/repository")
    public ResponseEntity<Void> updateRepository(
            @PathVariable Long studyHistoryId, @Valid @RequestBody RepositoryUpdateRequest request) throws IOException {
        studentStudyHistoryService.updateRepository(studyHistoryId, request);
        return ResponseEntity.ok().build();
    }
}
