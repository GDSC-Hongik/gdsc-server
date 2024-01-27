package com.gdschongik.gdsc.domain.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
	void 회원가입시_MemberRole은_GUEST이다() {
		// given
		Member member = Member.createGuestMember("testOauthId");

		// when
		MemberRole role = member.getRole();

		// then
		assertThat(role).isEqualTo(MemberRole.GUEST);
	}

	@Test
	void 회원가입시_MemberStatus는_NORMAL이다() {
		// given
		Member member = Member.createGuestMember("testOauthId");

		// when
		MemberStatus status = member.getStatus();

		// then
		assertThat(status).isEqualTo(MemberStatus.NORMAL);
	}
}