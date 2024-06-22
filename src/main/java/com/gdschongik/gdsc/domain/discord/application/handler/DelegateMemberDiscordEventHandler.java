package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelegateMemberDiscordEventHandler implements SpringEventHandler {

    private final DiscordUtil discordUtil;

    @Override
    public void delegate(Object context) {
        MemberRegularEvent event = (MemberRegularEvent) context;
        Guild guild = discordUtil.getCurrentGuild();
        // TODO: 이름이 아닌 ID로 찾기 위해 전체 멤버의 디스코드 사용자 ID를 저장해야 함
        Member member = discordUtil.getMemberByUsername(event.discordUsername());
        Role role = discordUtil.findRoleByName(MEMBER_ROLE_NAME);

        guild.addRoleToMember(member, role).queue();
    }
}
