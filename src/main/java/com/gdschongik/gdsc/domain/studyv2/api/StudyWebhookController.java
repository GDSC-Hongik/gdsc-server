package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.application.MentorStudyAnnouncementServiceV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.NotionWebhookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study Webhook", description = "스터디 웹훅 API입니다.")
@RestController
@RequestMapping("/webhook/study")
@RequiredArgsConstructor
public class StudyWebhookController {

    private final MentorStudyAnnouncementServiceV2 mentorStudyAnnouncementServiceV2;

    @Operation(summary = "스터디 노션 웹훅 처리", description = "공지 및 과제 발행 요청 웹훅을 처리합니다.")
    @PostMapping
    public ResponseEntity<Void> handleStudyWebhook(@RequestBody NotionWebhookRequest request) {
        mentorStudyAnnouncementServiceV2.createStudyAnnouncementByWebhook(request);
        return ResponseEntity.ok().build();
    }
}
