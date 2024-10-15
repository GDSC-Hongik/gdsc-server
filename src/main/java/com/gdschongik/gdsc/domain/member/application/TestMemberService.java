package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class TestMemberService {

    private final MemberRepository memberRepository;
    private final RestClient restClient;

    public TestMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.restClient = RestClient.builder().baseUrl("https://api.github.com").build();
    }

    @Transactional
    public void createTestMember(String githubHandle) {
        String githubOauthId = getGithubOauthId(githubHandle);

        if (memberRepository.findByOauthId(githubOauthId).isPresent()) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

        Member guestMember = Member.createGuest(githubOauthId);
        memberRepository.save(guestMember);
    }

    private String getGithubOauthId(String githubHandle) {
        return Optional.ofNullable(restClient
                        .get()
                        .uri("/users/{githubHandle}", githubHandle)
                        .retrieve()
                        .body(GithubUser.class))
                .map(GithubUser::id)
                .orElseThrow(() -> new CustomException(INTERNAL_SERVER_ERROR));
    }

    private record GithubUser(String id) {}
}
