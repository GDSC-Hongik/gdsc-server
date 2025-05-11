package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.MentorStudyHistoryService;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCompleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated
@Tag(name = "Study History V1 - Mentor", description = "멘토 스터디 수강 이력 API입니다.")
@RestController
@RequestMapping("/mentor/study-history")
@RequiredArgsConstructor
public class MentorStudyHistoryController {

    private final MentorStudyHistoryService mentorStudyHistoryService;

    @Operation(summary = "스터디 수료 처리", description = "스터디 수료 처리합니다.")
    @PostMapping("/complete")
    public ResponseEntity<Void> completeStudy(@RequestBody StudyCompleteRequest request) {
        mentorStudyHistoryService.completeStudy(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 수료 철회", description = "스터디 수료 처리를 철회합니다.")
    @PostMapping("/withdraw-completion")
    public ResponseEntity<Void> withdrawStudyCompletion(@RequestBody StudyCompleteRequest request) {
        mentorStudyHistoryService.withdrawStudyCompletion(request);
        return ResponseEntity.ok().build();
    }
}
