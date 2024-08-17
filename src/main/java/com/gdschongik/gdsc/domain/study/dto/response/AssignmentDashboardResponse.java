package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AssignmentDashboardResponse(
        @Schema(description = "레포지토리 링크") String repositoryLink,
        @Schema(description = "링크 수정 가능 여부") boolean isLinkEditable,
        @Schema(description = "제출 가능한 과제") List<AssignmentSubmittableDto> submittableAssignments) {
    public static AssignmentDashboardResponse of(
            String repositoryLink, boolean isAnySubmitted, List<AssignmentSubmittableDto> submittableAssignments) {
        return new AssignmentDashboardResponse(repositoryLink, !isAnySubmitted, submittableAssignments);
    }
}
