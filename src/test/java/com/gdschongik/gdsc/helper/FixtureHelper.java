package com.gdschongik.gdsc.helper;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.Member.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class FixtureHelper {

    public Member createAssociateMember(Long id) {
        Member member = createGuestMember(OAUTH_ID);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public RecruitmentRound createRecruitmentRound(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            Money fee) {
        Recruitment recruitment = Recruitment.createRecruitment(
                academicYear, semesterType, fee, FEE_NAME, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));

        return RecruitmentRound.create(RECRUITMENT_ROUND_NAME, startDate, endDate, recruitment, RoundType.FIRST);
    }

    public Membership createMembership(Member member, RecruitmentRound recruitmentRound) {
        return Membership.createMembership(member, recruitmentRound);
    }

    public IssuedCoupon createAndIssue(Money money, Member member) {
        Coupon coupon = Coupon.createCoupon("테스트쿠폰", money);
        return IssuedCoupon.issue(coupon, member);
    }
}
