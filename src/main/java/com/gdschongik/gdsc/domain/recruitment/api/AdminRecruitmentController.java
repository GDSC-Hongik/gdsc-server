package com.gdschongik.gdsc.domain.recruitment.api;

import com.gdschongik.gdsc.domain.recruitment.application.AdminRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateOrUpdateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.response.AdminRecruitmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Recruitment", description = "어드민 리쿠르팅 관리 API입니다.")
@RestController
@RequestMapping("/admin/recruitments")
@RequiredArgsConstructor
public class AdminRecruitmentController {

    private final AdminRecruitmentService adminRecruitmentService;

    @Operation(summary = "리쿠르팅 생성", description = "새로운 리쿠르팅(모집 기간)를 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createRecruitment(@Valid @RequestBody RecruitmentCreateOrUpdateRequest request) {
        adminRecruitmentService.createRecruitment(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리쿠르팅 목록 조회", description = "전체 리쿠르팅 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<AdminRecruitmentResponse>> getAllRecruitments() {
        List<AdminRecruitmentResponse> response = adminRecruitmentService.getAllRecruitments();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "리쿠르팅 수정", description = "기존 리쿠르팅(모집 기간)를 수정합니다.")
    @PutMapping("/{recruitmentId}")
    public ResponseEntity<Void> updateRecruitment(
            @PathVariable Long recruitmentId, @Valid @RequestBody RecruitmentCreateOrUpdateRequest request) {
        adminRecruitmentService.updateRecruitment(recruitmentId, request);
        return ResponseEntity.ok().build();
    }
}
