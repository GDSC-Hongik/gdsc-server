package com.gdschongik.gdsc.domain.study.api;

import com.gdschongik.gdsc.domain.study.application.AdminStudyService;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated
@Tag(name = "Study V1 - Admin", description = "어드민 스터디 API입니다.")
@RestController
@RequestMapping("/admin/studies")
@RequiredArgsConstructor
public class AdminStudyController {

    private final AdminStudyService adminStudyService;

    @Operation(summary = "스터디 개설", description = "수강신청을 위한 스터디를 개설합니다. 코어멤버만 스터디를 개설할 수 있습니다.")
    @PostMapping
    public ResponseEntity<Void> createStudy(@Valid @RequestBody StudyCreateRequest request) {
        adminStudyService.createStudyAndStudyDetail(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 스터디 조회", description = "모든 스터디를 조회합니다. 코어멤버만 접근 가능합니다.")
    @GetMapping
    public ResponseEntity<List<StudyResponse>> getStudies() {
        List<StudyResponse> response = adminStudyService.getAllStudies();
        return ResponseEntity.ok(response);
    }
}
