package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.MemberAdvanceToRegularEvent;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAdvanceToRegularEventHandler implements SpringEventHandler {

    private final DiscordUtil discordUtil;
    private final MemberRepository memberRepository;

    @Override
    public void delegate(Object context) {
        MemberAdvanceToRegularEvent event = (MemberAdvanceToRegularEvent) context;
        Guild guild = discordUtil.getCurrentGuild();
        // TODO: 이름이 아닌 ID로 찾기 위해 전체 멤버의 디스코드 사용자 ID를 저장해야 함
        Member member = discordUtil.getMemberByUsername(event.discordUsername());
        Role role = discordUtil.findRoleByName(MEMBER_ROLE_NAME);

        guild.addRoleToMember(member, role).queue();
        advanceToRegular(event.memberId());
    }

    private void advanceToRegular(Long memberId) {
        com.gdschongik.gdsc.domain.member.domain.Member currentMember = memberRepository
                .findById(Long.valueOf(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        try {
            currentMember.advanceToRegular();
        } catch (CustomException e) {
            log.info("{}", e.getErrorCode());
        }
    }
}
