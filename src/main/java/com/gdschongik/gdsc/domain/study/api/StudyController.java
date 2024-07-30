package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudyService;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study", description = "사용자 스터디 API입니다.")
@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/apply")
    public ResponseEntity<List<StudyResponse>> getAllApplicableStudies() {
        List<StudyResponse> response = studyService.getAllApplicableStudies();
        return ResponseEntity.ok().body(response);
    }
}