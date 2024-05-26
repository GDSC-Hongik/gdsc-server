package com.gdschongik.gdsc.domain.recruitment.api;

import com.gdschongik.gdsc.domain.recruitment.application.AdminRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Recruitment", description = "어드민 리쿠르팅 관리 API입니다.")
@RestController
@RequestMapping("/admin/recruitment")
@RequiredArgsConstructor
public class AdminRecruitmentController {

    private final AdminRecruitmentService adminRecruitmentService;

    @Operation(summary = "리쿠르팅 생성", description = "새로운 리쿠르팅(모집 기간)를 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createRecruitment(@Valid @RequestBody RecruitmentCreateRequest request) {
        adminRecruitmentService.createRecruitment(request);
        return ResponseEntity.ok().build();
    }
}
