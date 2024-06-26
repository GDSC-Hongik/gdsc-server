package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberDemoteRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminMemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminMemberService adminMemberService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    private Recruitment createRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            Money fee) {
        Recruitment recruitment =
                Recruitment.createRecruitment(name, startDate, endDate, academicYear, semesterType, roundType, fee);
        return recruitmentRepository.save(recruitment);
    }

    @Test
    void status가_DELETED라면_예외_발생() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);
        member.withdraw();
        memberRepository.save(member);

        // when & then
        MemberUpdateRequest requestBody = new MemberUpdateRequest(
                "A111111", "name", "010-1234-5678", Department.D001, "email@email.com", "discordUsername", "한글");
        assertThatThrownBy(() -> adminMemberService.updateMember(member.getId(), requestBody))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Nested
    class 준회원으로_일괄_강등시 {
        @Test
        void 해당_학기에_이미_시작된_모집기간이_있다면_실패한다() {
            // given
            createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            MemberDemoteRequest request = new MemberDemoteRequest(ACADEMIC_YEAR, SEMESTER_TYPE);

            // when & then
            assertThatThrownBy(() -> adminMemberService.demoteAllRegularMembersToAssociate(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_STARTDATE_ALREADY_PASSED.getMessage());
        }

        @Test
        void 해당_학기에_리쿠르팅이_존재하지_않는다면_실패한다() {
            // given
            MemberDemoteRequest request = new MemberDemoteRequest(ACADEMIC_YEAR, SEMESTER_TYPE);

            // when & then
            assertThatThrownBy(() -> adminMemberService.demoteAllRegularMembersToAssociate(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_FOUND.getMessage());
        }
    }
}
