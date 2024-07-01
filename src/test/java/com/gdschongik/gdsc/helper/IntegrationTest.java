package com.gdschongik.gdsc.helper;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.recruitment.application.OnboardingRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.global.security.PrincipalDetails;
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
    protected RecruitmentRoundRepository recruitmentRoundRepository;

    @MockBean
    protected OnboardingRecruitmentService onboardingRecruitmentService;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    protected void logoutAndReloginAs(Long memberId, MemberRole memberRole) {
        PrincipalDetails principalDetails = new PrincipalDetails(memberId, memberRole);
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

        return memberRepository.save(member);
    }

    protected RecruitmentRound createRecruitmentRound() {
        Recruitment recruitment = Recruitment.createRecruitment(ACADEMIC_YEAR, SEMESTER_TYPE, FEE);

        recruitmentRepository.save(recruitment);

        RecruitmentRound recruitmentRound =
                RecruitmentRound.create(NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

        return recruitmentRoundRepository.save(recruitmentRound);
    }

    protected RecruitmentRound createRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            Money fee) {
        Recruitment recruitment = Recruitment.createRecruitment(academicYear, semesterType, fee);
        recruitmentRepository.save(recruitment);

        RecruitmentRound recruitmentRound = RecruitmentRound.create(name, startDate, endDate, recruitment, roundType);
        return recruitmentRoundRepository.save(recruitmentRound);
    }
}
