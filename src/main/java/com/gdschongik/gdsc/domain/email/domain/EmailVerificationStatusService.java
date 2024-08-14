package com.gdschongik.gdsc.domain.email.domain;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.UnivVerificationStatus;
import com.gdschongik.gdsc.global.annotation.DomainService;
import java.util.Optional;

@DomainService
public class EmailVerificationStatusService {

    public UnivVerificationStatus determineStatus(
            Member member, Optional<UnivEmailVerification> univEmailVerification) {
        if (member.getAssociateRequirement().isUnivSatisfied()) {
            return UnivVerificationStatus.SATISFIED;
        } else {
            return univEmailVerification.isPresent()
                    ? UnivVerificationStatus.IN_PROGRESS
                    : UnivVerificationStatus.UNSATISFIED;
        }
    }
}
