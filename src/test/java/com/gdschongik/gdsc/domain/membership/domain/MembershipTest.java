package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MembershipTest {

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 역할이_GUEST라면_멤버십_가입신청에_실패한다() {
            // given
            Member guestMember = Member.createGuestMember(OAUTH_ID);
            Integer academicYear = 2024;
            SemesterType semesterType = SemesterType.FIRST;

            // when & then
            assertThatThrownBy(() -> Membership.createMembership(guestMember, academicYear, semesterType))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_NOT_APPLICABLE.getMessage());
        }
    }
}
