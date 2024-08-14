package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.StudyService;
import com.gdschongik.gdsc.domain.study.dto.request.StudyAttendRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study", description = "사용자 스터디 API입니다.")
@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @Operation(summary = "신청 가능한 스터디 조회", description = "모집 기간 중에 있는 스터디를 조회합니다.")
    @GetMapping("/apply")
    public ResponseEntity<List<StudyResponse>> getAllApplicableStudies() {
        List<StudyResponse> response = studyService.getAllApplicableStudies();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "스터디 수강신청", description = "스터디에 수강신청 합니다. 모집 기간 중이어야 하고, 이미 수강 중인 스터디가 없어야 합니다.")
    @PostMapping("/apply/{studyId}")
    public ResponseEntity<Void> applyStudy(@PathVariable Long studyId) {
        studyService.applyStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 출석체크", description = "스터디에 출석체크합니다. 현재 진행중인 스터디 회차에 출석체크해야 하며, 중복출석체크할 수 없습니다.")
    @PostMapping("/details/{studyDetailId}/attend")
    public ResponseEntity<Void> applyStudy(
            @PathVariable Long studyDetailId, @Valid @RequestBody StudyAttendRequest request) {
        studyService.attend(studyDetailId, request);
        return ResponseEntity.ok().build();
    }
}
