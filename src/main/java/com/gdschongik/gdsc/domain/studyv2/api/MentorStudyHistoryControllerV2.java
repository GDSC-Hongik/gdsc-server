package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.study.dto.request.StudyCompleteRequest;
import com.gdschongik.gdsc.domain.studyv2.application.MentorStudyHistoryServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor Study History V2", description = "멘토 스터디 수강 이력 API입니다.")
@RestController
@RequestMapping("/v2/mentor/study-histories")
@RequiredArgsConstructor
public class MentorStudyHistoryControllerV2 {

    private final MentorStudyHistoryServiceV2 mentorStudyHistoryServiceV2;

    @Operation(summary = "스터디 수료 처리", description = "스터디 수료 처리하고 스터디 수료 발급쿠폰을 발급합니다.")
    @PostMapping("/complete")
    public ResponseEntity<Void> completeStudy(@RequestBody StudyCompleteRequest request) {
        mentorStudyHistoryServiceV2.completeStudy(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 수료 철회", description = "스터디 수료 처리를 철회하고 스터디 수료 발급쿠폰을 회수합니다.")
    @PostMapping("/withdraw-completion")
    public ResponseEntity<Void> withdrawStudyCompletion(@RequestBody StudyCompleteRequest request) {
        mentorStudyHistoryServiceV2.withdrawStudyCompletion(request);
        return ResponseEntity.ok().build();
    }
}
