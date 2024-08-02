package com.gdschongik.gdsc.helper;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.discord.application.handler.DelegateMemberDiscordEventHandler;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberManageRole;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.MemberStudyRole;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.application.OnboardingRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.global.security.PrincipalDetails;
import com.gdschongik.gdsc.infra.feign.payment.client.PaymentClient;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected MembershipRepository membershipRepository;

    @Autowired
    protected CouponRepository couponRepository;

    @Autowired
    protected IssuedCouponRepository issuedCouponRepository;

    @Autowired
    protected RecruitmentRoundRepository recruitmentRoundRepository;

    @Autowired
    protected StudyRepository studyRepository;

    @Autowired
    protected StudyDetailRepository studyDetailRepository;

    @MockBean
    protected OnboardingRecruitmentService onboardingRecruitmentService;

    @MockBean
    protected PaymentClient paymentClient;

    @MockBean
    protected DelegateMemberDiscordEventHandler delegateMemberDiscordEventHandler;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
        doNothing().when(delegateMemberDiscordEventHandler).delegate(any());
    }

    protected void logoutAndReloginAs(Long memberId, MemberRole memberRole) {
        // TODO: MemberManageRole, MemberStudyRole 추가
        PrincipalDetails principalDetails =
                new PrincipalDetails(memberId, memberRole, MemberManageRole.NONE, MemberStudyRole.STUDENT);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected Member createMember() {
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);

        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.updateDiscordId(DISCORD_ID);

        return memberRepository.save(member);
    }

    protected Member createGuestMember() {
        Member guestMember = Member.createGuestMember(OAUTH_ID);
        return memberRepository.save(guestMember);
    }

    protected Member createAssociateMember() {
        Member member = createGuestMember();

        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();
        return memberRepository.save(member);
    }

    protected Member createRegularMember() {
        Member member = createAssociateMember();

        member.advanceToRegular();
        return memberRepository.save(member);
    }

    protected RecruitmentRound createRecruitmentRound() {
        Recruitment recruitment = createRecruitment(ACADEMIC_YEAR, SEMESTER_TYPE, FEE);

        RecruitmentRound recruitmentRound =
                RecruitmentRound.create(RECRUITMENT_ROUND_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

        return recruitmentRoundRepository.save(recruitmentRound);
    }

    protected RecruitmentRound createRecruitmentRound(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            Money fee) {
        Recruitment recruitment = createRecruitment(academicYear, semesterType, fee);

        RecruitmentRound recruitmentRound = RecruitmentRound.create(name, startDate, endDate, recruitment, roundType);
        return recruitmentRoundRepository.save(recruitmentRound);
    }

    protected Recruitment createRecruitment(Integer academicYear, SemesterType semesterType, Money fee) {
        Recruitment recruitment = Recruitment.createRecruitment(
                academicYear, semesterType, fee, FEE_NAME, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));
        return recruitmentRepository.save(recruitment);
    }

    protected Membership createMembership(Member member, RecruitmentRound recruitmentRound) {
        Membership membership = Membership.createMembership(member, recruitmentRound);
        return membershipRepository.save(membership);
    }

    protected IssuedCoupon createAndIssue(Money money, Member member) {
        Coupon coupon = Coupon.createCoupon("테스트쿠폰", money);
        couponRepository.save(coupon);
        IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);
        return issuedCouponRepository.save(issuedCoupon);
    }

    protected Study createStudy(Member mentor, Period period, Period applicationPeriod) {
        Study study = Study.createStudy(
                ACADEMIC_YEAR,
                SEMESTER_TYPE,
                mentor,
                period,
                applicationPeriod,
                TOTAL_WEEK,
                ONLINE_STUDY,
                DAY_OF_WEEK,
                STUDY_START_TIME,
                STUDY_END_TIME);

        return studyRepository.save(study);
    }

    protected StudyDetail createStudyDetail(Study study, LocalDateTime startDate, LocalDateTime endDate) {
        StudyDetail studyDetail =
                StudyDetail.createStudyDetail(study, 1L, ATTENDANCE_NUMBER, Period.createPeriod(startDate, endDate));
        return studyDetailRepository.save(studyDetail);
    }
}
