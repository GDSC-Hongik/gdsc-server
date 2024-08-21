package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.CommonStudyService;
import com.gdschongik.gdsc.domain.study.dto.response.CommonStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyAnnouncementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Common Study", description = "공통 스터디 API입니다.")
@RestController
@RequestMapping("/common/studies")
@RequiredArgsConstructor
public class CommonStudyController {

    private final CommonStudyService commonStudyService;

    @Operation(summary = "스터디 기본 정보 조회", description = "스터디 기본 정보를 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<CommonStudyResponse> getStudyInformation(@PathVariable Long studyId) {
        CommonStudyResponse response = commonStudyService.getStudyInformation(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 공지 목록 조회", description = "스터디 공지 목록을 조회합니다.")
    @GetMapping("/{studyId}/announcements")
    public ResponseEntity<List<StudyAnnouncementResponse>> getStudyAnnouncements(@PathVariable Long studyId) {
        List<StudyAnnouncementResponse> response = commonStudyService.getStudyAnnouncements(studyId);
        return ResponseEntity.ok(response);
    }
}
