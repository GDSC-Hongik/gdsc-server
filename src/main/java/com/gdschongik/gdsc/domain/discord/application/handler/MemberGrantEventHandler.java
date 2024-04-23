package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.member.domain.MemberGrantEvent;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberGrantEventHandler implements SpringEventHandler {

    private final DiscordUtil discordUtil;

    @Override
    public void delegate(Object context) {
        MemberGrantEvent event = (MemberGrantEvent) context;
        Guild guild = discordUtil.getCurrentGuild();
        Member member = guild.getMemberById(event.discordUsername());
        Role role = discordUtil.findRoleByName(MEMBER_ROLE_NAME);

        guild.addRoleToMember(member, role).queue();
    }
}
