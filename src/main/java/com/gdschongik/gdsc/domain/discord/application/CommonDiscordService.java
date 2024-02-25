package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDiscordService {

    private final MemberRepository memberRepository;

    public String getNicknameByDiscordUsername(String discordUsername) {
        Member member = memberRepository
                .findByDiscordUsername(discordUsername)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return member.getNickname();
    }
}
