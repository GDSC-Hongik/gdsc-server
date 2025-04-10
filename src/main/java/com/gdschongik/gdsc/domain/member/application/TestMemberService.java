package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestMemberService {

    private final MemberRepository memberRepository;
    private final GithubClient githubClient;

    @Transactional
    public void createTestMember(String githubHandle) {
        String githubOauthId = githubClient.getOauthId(githubHandle);

        if (memberRepository.findByOauthId(githubOauthId).isPresent()) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

        Member guestMember = Member.createGuest(githubOauthId);
        memberRepository.save(guestMember);
    }
}
