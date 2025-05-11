package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.MentorStudyService;
import com.gdschongik.gdsc.domain.study.dto.request.StudyAnnouncementCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Deprecated
@Tag(name = "Study V1 - Mentor", description = "멘토 스터디 API입니다.")
@RestController
@RequestMapping("/mentor/studies")
@RequiredArgsConstructor
public class MentorStudyController {

    private final MentorStudyService mentorStudyService;

    @Operation(summary = "스터디 정보 작성", description = "스터디 기본 정보와 상세 정보를 작성합니다.")
    @PatchMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(@PathVariable Long studyId, @RequestBody StudyUpdateRequest request) {
        mentorStudyService.updateStudy(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 스터디 조회", description = "내가 멘토로 있는 스터디를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<StudyResponse>> getStudiesInCharge() {
        List<StudyResponse> response = mentorStudyService.getStudiesInCharge();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 수강생 관리", description = "해당 스터디의 수강생을 관리합니다")
    @GetMapping("/{studyId}/students")
    public ResponseEntity<Page<StudyStudentResponse>> getStudyStudents(@PathVariable Long studyId, Pageable pageable) {
        Page<StudyStudentResponse> response = mentorStudyService.getStudyStudents(studyId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 공지 생성", description = "스터디의 공지사항을 생성합니다.")
    @PostMapping("/{studyId}/announcements")
    public ResponseEntity<Void> createStudyAnnouncement(
            @PathVariable Long studyId, @Valid @RequestBody StudyAnnouncementCreateUpdateRequest request) {
        mentorStudyService.createStudyAnnouncement(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 수정", description = "스터디의 공지사항을 수정합니다.")
    @PutMapping("/announcements/{studyAnnouncementId}")
    public ResponseEntity<Void> updateStudyAnnouncement(
            @PathVariable Long studyAnnouncementId, @Valid @RequestBody StudyAnnouncementCreateUpdateRequest request) {
        mentorStudyService.updateStudyAnnouncement(studyAnnouncementId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 공지 삭제", description = "스터디의 공지사항을 삭제합니다.")
    @DeleteMapping("/announcements/{studyAnnouncementId}")
    public ResponseEntity<Void> deleteStudyAnnouncement(@PathVariable Long studyAnnouncementId) {
        mentorStudyService.deleteStudyAnnouncement(studyAnnouncementId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수강생 정보 엑셀 다운로드", description = "수강생 정보를 엑셀로 다운로드합니다.")
    @GetMapping("/{studyId}/students/excel")
    public ResponseEntity<byte[]> createStudyWorkbook(@PathVariable Long studyId) {
        byte[] response = mentorStudyService.createStudyExcel(studyId);
        ContentDisposition contentDisposition =
                ContentDisposition.builder("attachment").filename("study.xls").build();
        return ResponseEntity.ok()
                .headers(httpHeaders -> {
                    httpHeaders.setContentDisposition(contentDisposition);
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    httpHeaders.setContentLength(response.length);
                })
                .body(response);
    }
}
