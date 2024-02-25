package com.gdschongik.gdsc.domain.discord.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDiscordService {

    private final MemberRepository memberRepository;

    public String getNicknameByDiscordUsername(String discordUsername) {
        return memberRepository
                .findByDiscordUsername(discordUsername)
                .map(Member::getNickname)
                .orElse(null);
    }
}
