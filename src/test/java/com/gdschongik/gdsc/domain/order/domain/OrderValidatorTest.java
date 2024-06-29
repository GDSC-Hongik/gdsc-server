package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import java.time.LocalDateTime;

class OrderValidatorTest {

    OrderValidator orderValidator = new OrderValidator();

    private Member createAssociateMember() {
        Member member = Member.createGuestMember(OAUTH_ID);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();
        return member;
    }

    private Recruitment createRecruitment(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            Money fee) {
        return Recruitment.createRecruitment(
                RECRUITMENT_NAME, startDate, endDate, academicYear, semesterType, RoundType.FIRST, fee);
    }

    private Membership createMembership(Member member, Recruitment recruitment) {
        return Membership.createMembership(member, recruitment);
    }
}
