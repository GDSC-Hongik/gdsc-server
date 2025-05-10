package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.CommonStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyCommonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study V2 - Common", description = "공통 스터디 V2 API입니다.")
@RestController
@RequestMapping("/v2/common/studies")
@RequiredArgsConstructor
public class CommonStudyControllerV2 {

    private final CommonStudyServiceV2 commonStudyService;

    @Operation(summary = "스터디 기본 정보 조회", description = "스터디 기본 정보를 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyCommonDto> getStudyInformation(@PathVariable Long studyId) {
        var response = commonStudyService.getStudyInformation(studyId);
        return ResponseEntity.ok(response);
    }
}
