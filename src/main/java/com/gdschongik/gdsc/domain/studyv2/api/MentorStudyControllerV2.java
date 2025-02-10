package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.MentorStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyManagerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor Study V2", description = "스터디 V2 멘토 API입니다.")
@RestController
@RequestMapping("/v2/mentor/studies")
@RequiredArgsConstructor
public class MentorStudyControllerV2 {

    private final MentorStudyServiceV2 mentorStudyServiceV2;

    @Operation(summary = "내 스터디 조회", description = "내가 멘토로 있는 스터디를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<StudyManagerResponse>> getStudiesInCharge() {
        var response = mentorStudyServiceV2.getStudiesInCharge();
        return ResponseEntity.ok(response);
    }
}
