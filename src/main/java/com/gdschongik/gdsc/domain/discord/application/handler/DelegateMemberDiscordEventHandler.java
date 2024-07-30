package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.member.domain.MemberAdvancedToRegularEvent;
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
public class DelegateMemberDiscordEventHandler implements SpringEventHandler {

    private final DiscordUtil discordUtil;

    @Override
    public void delegate(Object context) {
        MemberAdvancedToRegularEvent event = (MemberAdvancedToRegularEvent) context;
        Guild guild = discordUtil.getCurrentGuild();
        Member member = discordUtil.getMemberById(event.discordId());
        Role role = discordUtil.findRoleByName(MEMBER_ROLE_NAME);

        guild.addRoleToMember(member, role).queue();

        log.info(
                "[DelegateMemberDiscordEventHandler] 디스코드 서버 정회원 역할 부여 완료: memberId={}, discordId={}",
                event.memberId(),
                event.discordId());
    }
}
