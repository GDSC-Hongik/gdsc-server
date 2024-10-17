package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.MentorStudyDetailService;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyCurriculumResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyMentorAttendanceResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStatisticsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mentor StudyDetail", description = "멘토 스터디 상세 관리 API입니다.")
@RestController
@RequestMapping("/mentor/study-details")
@RequiredArgsConstructor
public class MentorStudyDetailController {

    private final MentorStudyDetailService mentorStudyDetailService;

    @Operation(summary = "스터디 과제 수정", description = "멘토만 과제를 수정할 수 있습니다.")
    @PatchMapping("/{studyDetailId}/assignments")
    public ResponseEntity<AssignmentResponse> updateStudyAssignment(
            @PathVariable Long studyDetailId, @Valid @RequestBody AssignmentCreateUpdateRequest request) {
        AssignmentResponse response = mentorStudyDetailService.updateStudyAssignment(studyDetailId, request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "스터디 과제 개설", description = "멘토만 과제를 개설할 수 있습니다.")
    @PutMapping("/{studyDetailId}/assignments")
    public ResponseEntity<AssignmentResponse> publishStudyAssignment(
            @PathVariable Long studyDetailId, @Valid @RequestBody AssignmentCreateUpdateRequest request) {
        AssignmentResponse response = mentorStudyDetailService.publishStudyAssignment(studyDetailId, request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "스터디 주차별 과제 목록 조회", description = "주차별 스터디 과제 목록을 조회합니다.")
    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentResponse>> getWeeklyAssignments(@RequestParam(name = "studyId") Long studyId) {
        List<AssignmentResponse> response = mentorStudyDetailService.getWeeklyAssignments(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 과제 상세 조회", description = "멘토가 자신의 스터디 과제를 조회합니다.")
    @GetMapping("/{studyDetailId}/assignments")
    public ResponseEntity<AssignmentResponse> getStudyAssignment(@PathVariable Long studyDetailId) {
        AssignmentResponse response = mentorStudyDetailService.getAssignment(studyDetailId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 과제 휴강 처리", description = "해당 주차 과제를 휴강 처리합니다.")
    @PatchMapping("/{studyDetailId}/assignments/cancel")
    public ResponseEntity<Void> cancelStudyAssignment(@PathVariable Long studyDetailId) {
        mentorStudyDetailService.cancelStudyAssignment(studyDetailId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "스터디 주차별 커리큘럼 목록 조회", description = "멘토가 자신의 스터디 커리큘럼 목록을 조회합니다")
    @GetMapping("/curriculums")
    public ResponseEntity<List<StudyCurriculumResponse>> getStudyCurriculums(
            @RequestParam(name = "studyId") Long studyId) {
        List<StudyCurriculumResponse> response = mentorStudyDetailService.getCurriculums(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 주차별 출결번호 조회", description = "멘토가 자신의 스터디 출결번호 목록을 조회합니다. 지난 출석은 목록에서 제외합니다.")
    @GetMapping("/attendances")
    public ResponseEntity<List<StudyMentorAttendanceResponse>> getAttendanceNumbers(
            @RequestParam(name = "studyId") Long studyId) {
        List<StudyMentorAttendanceResponse> response = mentorStudyDetailService.getAttendanceNumbers(studyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "스터디 통계 조회", description = "멘토가 자신의 스터디 출석률, 과제 제출률, 수료율에 대한 통계를 조회합니다. 휴강 주차는 계산에서 제외합니다.")
    @GetMapping("/statistics")
    public ResponseEntity<StudyStatisticsResponse> getStudyStatistics(@RequestParam(name = "studyId") Long studyId) {
        StudyStatisticsResponse response = mentorStudyDetailService.getStudyStatistics(studyId);
        return ResponseEntity.ok(response);
    }
}
