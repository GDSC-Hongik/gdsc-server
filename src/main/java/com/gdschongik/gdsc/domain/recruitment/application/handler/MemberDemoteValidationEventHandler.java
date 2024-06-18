package com.gdschongik.gdsc.domain.recruitment.application.handler;

import com.gdschongik.gdsc.domain.member.domain.MemberDemoteValidationEvent;
import com.gdschongik.gdsc.domain.recruitment.application.AdminRecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDemoteValidationEventHandler {
    private final AdminRecruitmentService adminRecruitmentService;

    @EventListener
    public void validateRegularMembersDemoteAvailable(MemberDemoteValidationEvent memberDemoteValidationEvent) {
        adminRecruitmentService.validateRecruitmentNotStarted(
                memberDemoteValidationEvent.academicYear(), memberDemoteValidationEvent.semesterType());
    }
}
