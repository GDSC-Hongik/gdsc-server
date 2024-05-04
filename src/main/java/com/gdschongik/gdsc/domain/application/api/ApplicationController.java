package com.gdschongik.gdsc.domain.application.api;

import com.gdschongik.gdsc.domain.application.application.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application", description = "가입 신청 API입니다.")
@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "새학기 가입 신청서 접수", description = "새학기 가입 신청서를 접수합니다.")
    @PostMapping("/receive")
    public ResponseEntity<Void> receive() {
        applicationService.receive();
        return ResponseEntity.ok().build();
    }
}