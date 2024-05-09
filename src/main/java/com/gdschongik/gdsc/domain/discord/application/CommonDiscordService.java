package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDiscordService {

    private final MemberRepository memberRepository;
    private final DiscordUtil discordUtil;

    public String getNicknameByDiscordUsername(String discordUsername) {
        return memberRepository
                .findByDiscordUsername(discordUsername)
                .map(Member::getNickname)
                .orElse(null);
    }

    public void batchDiscordId(RequirementStatus discordStatus) {
        List<Member> discordVerifiedMembers = memberRepository.findAllByDiscordStatus(discordStatus);

        discordVerifiedMembers.forEach(member -> {
            String discordUsername = member.getDiscordUsername();
            String discordId = discordUtil.getMemberIdByUsername(discordUsername);
            member.updateDiscordId(discordId);
        });

        memberRepository.saveAll(discordVerifiedMembers);
    }

    public void checkPermissionForCommand(String discordUsername) {
        Member member = memberRepository
                .findByDiscordUsername(discordUsername)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if (member.getRole() != MemberRole.ADMIN) {
            throw new CustomException(INVALID_ROLE);
        }
    }
}
