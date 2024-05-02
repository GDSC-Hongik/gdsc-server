package com.gdschongik.gdsc.domain.application.domain.dto.response;

import com.gdschongik.gdsc.domain.application.domain.Application;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;

public record ApplicationResponse(Long applicationId, Long memberId, RequirementStatus status) {
    public static ApplicationResponse from(Application application) {
        return new ApplicationResponse(
                application.getId(), application.getMember().getId(), application.getPaymentStatus());
    }
}
