package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.AdminStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Study V2", description = "스터디 V2 어드민 API입니다.")
@RestController
@RequestMapping("/admin/studies/v2")
@RequiredArgsConstructor
public class AdminStudyControllerV2 {

    private final AdminStudyServiceV2 adminStudyServiceV2;

    @Operation(summary = "스터디 개설", description = "스터디를 개설합니다. 빈 스터디회차를 함께 생성합니다. 과제 스터디의 경우 라이브 세션 관련 필드는 null이어야 합니다.")
    @PostMapping
    public ResponseEntity<Void> createStudy(@Valid @RequestBody StudyCreateRequest request) {
        adminStudyServiceV2.createStudy(request);
        return ResponseEntity.ok().build();
    }
}
