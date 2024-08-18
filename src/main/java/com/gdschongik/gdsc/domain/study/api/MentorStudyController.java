package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.MentorStudyService;
import com.gdschongik.gdsc.domain.study.dto.request.StudyDetailUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.MentorStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mentor Study", description = "멘토 스터디 API입니다.")
@RestController
@RequestMapping("/mentor/studies")
@RequiredArgsConstructor
public class MentorStudyController {

    private final MentorStudyService mentorStudyService;

    @Operation(summary = "스터디 상세 정보 작성", description = "스터디 상세 정보를 작성합니다.")
    @PatchMapping("/{studyId}")
    public ResponseEntity<Void> updateStudyDetail(
            @PathVariable Long studyId, @RequestBody StudyDetailUpdateRequest request) {
        mentorStudyService.updateStudyDetail(studyId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 스터디 조회", description = "내가 멘토로 있는 스터디를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<MentorStudyResponse>> getStudiesInCharge() {
        List<MentorStudyResponse> response = mentorStudyService.getStudiesInCharge();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 수강생 명단 조회", description = "해당 스터디의 수강생 명단을 조회합니다")
    @GetMapping("/{studyId}/students")
    public ResponseEntity<List<StudyStudentResponse>> getStudyStudents(@PathVariable Long studyId) {
        List<StudyStudentResponse> response = mentorStudyService.getStudyStudents(studyId);
        return ResponseEntity.ok(response);
    }
}
