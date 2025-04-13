package com.gdschongik.gdsc.domain.studyv2.api;

import com.gdschongik.gdsc.domain.studyv2.dto.request.NotionWebhookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study Announcement Webhook", description = "스터디 공지 Webhook API입니다.")
@RestController
@RequestMapping("/study-announcements/webhook")
@RequiredArgsConstructor
public class StudyAnnouncementWebhookController {

    @Operation(summary = "스터디 공지 Webhook", description = "스터디 공지 Webhook API입니다.")
    @PostMapping
    public ResponseEntity<Void> getStudyAnnouncementWebhook(@RequestBody NotionWebhookRequest request) {
        System.out.println("request = " + request);
        return ResponseEntity.ok().build();
    }
}
