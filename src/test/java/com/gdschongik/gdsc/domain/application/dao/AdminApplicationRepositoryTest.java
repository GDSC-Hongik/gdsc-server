package com.gdschongik.gdsc.domain.application.dao;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.OAUTH_ID;

import com.gdschongik.gdsc.domain.application.domain.Application;
import com.gdschongik.gdsc.domain.application.domain.dto.request.ApplicationQueryOption;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.repository.RepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class AdminApplicationRepositoryTest extends RepositoryTest {
    private ApplicationQueryOption EMPTY_QUERY_OPTION = new ApplicationQueryOption(null, null);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Application setFixture() {
        Member member = Member.createGuestMember(OAUTH_ID);
        Member newMember = memberRepository.save(member);
        Application application = Application.createApplication(member);
        return applicationRepository.save(application);
    }

    private void flushAndClearBeforeExecute() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Nested
    class 가입신청_조회할때 {
        @Test
        void 회비납부_안했으면_PENDING으로_조회_성공() {
            // given
            Application application = setFixture();

            // when
            Page<Application> applications = applicationRepository.findAllByPaymentStatus(
                    EMPTY_QUERY_OPTION, RequirementStatus.PENDING, PageRequest.of(0, 10));

            // then
            Assertions.assertThat(applications).contains(application);
        }

        @Test
        void 회비납부_안헀으면_VERIFIED로_조회되지_않는다() {
            // given
            Application application = setFixture();

            // when
            Page<Application> applications = applicationRepository.findAllByPaymentStatus(
                    EMPTY_QUERY_OPTION, RequirementStatus.VERIFIED, PageRequest.of(0, 10));

            // then
            Assertions.assertThat(applications).doesNotContain(application);
        }
    }
}
