package com.gdschongik.gdsc.domain.member.dao;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.helper.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private MemberQueryRequest getEmptyQueryRequest() {
        return new MemberQueryRequest(null, null, null, null, null, null, null);
    }

    private Pageable getDefaultPageable() {
        return PageRequest.of(0, 100);
    }

    @BeforeEach
    void setUp() {
        Member member1 = Member.createGuestMember("test1");
        Member member2 = Member.createGuestMember("test2");
        Member member3 = Member.createGuestMember("test3");
        Member member4 = Member.createGuestMember("test4");

        member1.completeUnivEmailVerification("test1@g.hongik.ac.kr");
        member2.completeUnivEmailVerification("test2@g.hongik.ac.kr");
        member3.completeUnivEmailVerification("test3@g.hongik.ac.kr");
        member4.completeUnivEmailVerification("test4@g.hongik.ac.kr");

        member1.signup("C111001", "name1", "01000000001", Department.D001, "test1@email.com");
        member2.signup("C111002", "name2", "01000000002", Department.D002, "test2@email.com");
        member3.signup("C111003", "name3", "01000000003", Department.D003, "test3@email.com");
        member4.signup("C111004", "name4", "01000000004", Department.D004, "test4@email.com");

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Nested
    class 승인가능한_멤버를_조회하면 {

        @BeforeEach
        void setUp() {
            Member member1 = em.find(Member.class, 1L);
            member1.verifyDiscord("testusername", "testnickname");
            member1.updatePaymentStatus(RequirementStatus.VERIFIED);
            member1.verifyBevy();
        }

        @Test
        void 멤버역할이_GUEST이다() {
            // given & when
            Page<Member> members = memberRepository.findAllGrantable(getEmptyQueryRequest(), getDefaultPageable());

            // then
            assertThat(members).hasSize(1).allMatch(member -> member.getRole() == MemberRole.GUEST);
        }

        @Test
        void 모든_가입조건이_VERIFIED이다() {
            // given & when
            Page<Member> members = memberRepository.findAllGrantable(getEmptyQueryRequest(), getDefaultPageable());

            // then
            assertThat(members)
                    .hasSize(1)
                    .allMatch(member -> member.getRequirement().isUnivVerified())
                    .allMatch(member -> member.getRequirement().isDiscordVerified())
                    .allMatch(member -> member.getRequirement().isPaymentVerified())
                    .allMatch(member -> member.getRequirement().isBevyVerified());
        }
    }
}
