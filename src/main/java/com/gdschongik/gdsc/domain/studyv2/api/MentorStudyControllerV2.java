package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.MentorStudyServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor Study V2 ", description = "스터디 V2 멘토 API입니다.")
@RestController
@RequestMapping("/v2/mentor/studies")
@RequiredArgsConstructor
public class MentorStudyControllerV2 {

    private final MentorStudyServiceV2 mentorStudyServiceV2;

    @Operation(summary = "스터디 정보 작성", description = "스터디 정보를 작성합니다.")
    @PutMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(@PathVariable Long studyId, @RequestBody StudyUpdateRequest request) {
        mentorStudyServiceV2.updateStudy(studyId, request);
        return ResponseEntity.ok().build();
    }
}
