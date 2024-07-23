package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.discord.domain.DiscordValidator;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonDiscordService {

    private final MemberRepository memberRepository;
    private final DiscordUtil discordUtil;
    private final DiscordValidator discordValidator;

    public String getNicknameByDiscordUsername(String discordUsername) {
        return memberRepository
                .findByDiscordUsername(discordUsername)
                .map(Member::getNickname)
                .orElse(null);
    }

    @Transactional
    public void batchDiscordId(String currentDiscordUsername, RequirementStatus discordStatus) {
        Member currentMember = memberRepository
                .findByDiscordUsername(currentDiscordUsername)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        discordValidator.validatePermissionForCommand(currentMember);

        List<Member> discordSatisfiedMembers = memberRepository.findAllByDiscordStatus(discordStatus);

        discordSatisfiedMembers.forEach(member -> {
            String discordUsername = member.getDiscordUsername();
            String discordId = discordUtil.getMemberIdByUsername(discordUsername);
            member.updateDiscordId(discordId);
        });
    }
}
