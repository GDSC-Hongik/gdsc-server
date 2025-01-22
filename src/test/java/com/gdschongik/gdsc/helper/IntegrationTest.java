package com.gdschongik.gdsc.helper;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.global.common.constant.CouponConstant.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.CouponType;
import com.gdschongik.gdsc.domain.coupon.domain.IssuanceMethodType;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.discord.application.handler.DelegateMemberDiscordEventHandler;
import com.gdschongik.gdsc.domain.discord.application.handler.MemberDiscordRoleRevokeHandler;
import com.gdschongik.gdsc.domain.discord.application.handler.SpringEventHandler;
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
import com.gdschongik.gdsc.domain.study.dao.StudyAchievementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.global.security.PrincipalDetails;
import com.gdschongik.gdsc.infra.feign.payment.client.PaymentClient;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
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
    protected RedisCleaner redisCleaner;

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

    @Autowired
    protected StudyHistoryRepository studyHistoryRepository;

    @Autowired
    protected StudyAchievementRepository studyAchievementRepository;

    @MockBean
    protected OnboardingRecruitmentService onboardingRecruitmentService;

    @MockBean
    protected PaymentClient paymentClient;

    @MockBean
    protected GithubClient githubClient;

    @MockBean
    protected DelegateMemberDiscordEventHandler delegateMemberDiscordEventHandler;

    @MockBean
    protected MemberDiscordRoleRevokeHandler memberDiscordRoleRevokeHandler;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
        redisCleaner.execute();
        doStubDiscordEventHandler();
        doStubTemplate();
    }

    /**
     * stubbing에 사용할 템플릿 메서드입니다.
     * 하위 클래스에서 이 메서드를 오버라이드하여 stubbing을 수행합니다.
     * 오버라이드된 경우, `@BeforeEach`의 맨 마지막에 호출됩니다.
     */
    protected void doStubTemplate() {
        // 기본적으로 아무 것도 하지 않습니다. 필요한 경우에만 오버라이드하여 사용합니다.
    }

    /**
     * {@link SpringEventHandler#delegate} 메서드를 stubbing합니다.
     * 해당 핸들러 메서드는 스프링 이벤트를 수신하여 JDA를 통해 디스코드 관련 로직을 처리합니다.
     * JDA는 외부 API의 커넥션을 필요로 하기 때문에 테스트에서는 `@MockBean`으로 주입한 핸들러를 stubbing하여 verify로 호출 여부만 확인합니다.
     * 기본적으로 아무 것도 하지 않도록 설정합니다.
     */
    private void doStubDiscordEventHandler() {
        doNothing().when(delegateMemberDiscordEventHandler).delegate(any());
        doNothing().when(memberDiscordRoleRevokeHandler).delegate(any());
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
        Member member = Member.createGuest(OAUTH_ID);
        memberRepository.save(member);

        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.updateDiscordId(DISCORD_ID);

        return memberRepository.save(member);
    }

    protected Member createGuestMember() {
        Member guestMember = Member.createGuest(OAUTH_ID);
        return memberRepository.save(guestMember);
    }

    protected Member createAssociateMember() {
        Member member = createGuestMember();

        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.advanceToAssociate();
        return memberRepository.save(member);
    }

    protected Member createRegularMember() {
        Member member = createAssociateMember();

        member.advanceToRegular();
        return memberRepository.save(member);
    }

    public Member createMentor() {
        Member member = createAssociateMember();
        member.assignToMentor();
        return memberRepository.save(member);
    }

    protected RecruitmentRound createRecruitmentRound() {
        Recruitment recruitment = createRecruitment(ACADEMIC_YEAR, SEMESTER_TYPE, FEE);

        RecruitmentRound recruitmentRound =
                RecruitmentRound.create(RECRUITMENT_ROUND_NAME, START_TO_END_PERIOD, recruitment, ROUND_TYPE);

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

        RecruitmentRound recruitmentRound =
                RecruitmentRound.create(name, Period.of(startDate, endDate), recruitment, roundType);
        return recruitmentRoundRepository.save(recruitmentRound);
    }

    protected Recruitment createRecruitment(Integer academicYear, SemesterType semesterType, Money fee) {
        Recruitment recruitment = Recruitment.create(
                academicYear, semesterType, fee, FEE_NAME, Period.of(SEMESTER_START_DATE, SEMESTER_END_DATE));
        return recruitmentRepository.save(recruitment);
    }

    protected Membership createMembership(Member member, RecruitmentRound recruitmentRound) {
        Membership membership = Membership.create(member, recruitmentRound);
        return membershipRepository.save(membership);
    }

    protected IssuedCoupon createAndIssue(Money money, Member member, CouponType couponType, Study study) {
        Coupon coupon = Coupon.create(COUPON_NAME, money, couponType, IssuanceMethodType.AUTOMATIC, study);
        couponRepository.save(coupon);
        IssuedCoupon issuedCoupon = IssuedCoupon.create(coupon, member);
        return issuedCouponRepository.save(issuedCoupon);
    }

    protected Study createStudy(Member mentor, Period period, Period applicationPeriod) {
        Study study = Study.create(
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

        return studyRepository.save(study);
    }

    protected Study createNewStudy(Member mentor, Long totalWeek, Period period, Period applicationPeriod) {
        Study study = Study.create(
                ACADEMIC_YEAR,
                SEMESTER_TYPE,
                STUDY_TITLE,
                mentor,
                period,
                applicationPeriod,
                totalWeek,
                ONLINE_STUDY,
                DAY_OF_WEEK,
                STUDY_START_TIME,
                STUDY_END_TIME);

        return studyRepository.save(study);
    }

    protected StudyDetail createStudyDetail(Study study, LocalDateTime startDate, LocalDateTime endDate) {
        StudyDetail studyDetail = StudyDetail.create(study, 1L, ATTENDANCE_NUMBER, Period.of(startDate, endDate));
        return studyDetailRepository.save(studyDetail);
    }

    protected StudyDetail createNewStudyDetail(Long week, Study study, LocalDateTime startDate, LocalDateTime endDate) {
        StudyDetail studyDetail = StudyDetail.create(study, week, ATTENDANCE_NUMBER, Period.of(startDate, endDate));
        return studyDetailRepository.save(studyDetail);
    }

    protected StudyHistory createStudyHistory(Member member, Study study) {
        StudyHistory studyHistory = StudyHistory.create(member, study);
        return studyHistoryRepository.save(studyHistory);
    }

    protected StudyAchievement createStudyAchievement(Member member, Study study, AchievementType achievementType) {
        StudyAchievement studyAchievement = StudyAchievement.create(member, study, achievementType);
        return studyAchievementRepository.save(studyAchievement);
    }

    protected StudyDetail publishAssignment(StudyDetail studyDetail) {
        studyDetail.publishAssignment(ASSIGNMENT_TITLE, studyDetail.getPeriod().getEndDate(), DESCRIPTION_LINK);
        return studyDetailRepository.save(studyDetail);
    }
}
