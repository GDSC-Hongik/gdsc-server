package com.gdschongik.gdsc.helper;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.domain.member.domain.MemberManageRole.ADMIN;
import static com.gdschongik.gdsc.domain.member.domain.MemberStudyRole.MENTOR;

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
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class FixtureHelper {

    public Member createGuestMember(Long id) {
        Member member = Member.createGuestMember(OAUTH_ID);
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public Member createAssociateMember(Long id) {
        Member member = createGuestMember(id);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();
        return member;
    }

    public Member createRegularMember(Long id) {
        Member member = createAssociateMember(id);
        member.advanceToRegular();
        return member;
    }

    public Member createAdmin(Long id) {
        Member member = createRegularMember(id);
        ReflectionTestUtils.setField(member, "manageRole", ADMIN);
        return member;
    }

    public Member createMentor(Long id) {
        Member member = createRegularMember(id);
        member.assignToMentor();
        ReflectionTestUtils.setField(member, "studyRole", MENTOR);
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

    public Study createStudy(Member mentor, Period period, Period applicationPeriod) {
        return Study.createStudy(
                ACADEMIC_YEAR,
                SEMESTER_TYPE,
                STUDY_TITLE,
                mentor,
                period,
                applicationPeriod,
                TOTAL_WEEK,
                ONLINE_STUDY,
                DAY_OF_WEEK,
                STUDY_START_TIME,
                STUDY_END_TIME);
    }

    public Study createStudyWithMentor(Long mentorId, Period period, Period applicationPeriod) {
        Member mentor = createAssociateMember(mentorId);
        return createStudy(mentor, period, applicationPeriod);
    }

    public StudyDetail createStudyDetail(Study study, LocalDateTime startDate, LocalDateTime endDate) {
        return StudyDetail.createStudyDetail(study, 1L, ATTENDANCE_NUMBER, Period.createPeriod(startDate, endDate));
    }

    public StudyDetail createStudyDetailWithAssignment(
            Study study, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadline) {
        StudyDetail studyDetail = createStudyDetail(study, startDate, endDate);
        studyDetail.publishAssignment(ASSIGNMENT_TITLE, deadline, DESCRIPTION_LINK);
        return studyDetail;
    }
}
