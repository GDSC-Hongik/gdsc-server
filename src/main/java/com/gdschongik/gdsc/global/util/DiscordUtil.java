package com.gdschongik.gdsc.global.util;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.property.DiscordProperty;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@RequiredArgsConstructor
public class DiscordUtil {

    private final JDA jda;
    private final DiscordProperty discordProperty;

    public Role findRoleByName(String roleName) {
        return jda.getRolesByName(roleName, true).stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DISCORD_ROLE_NOT_FOUND));
    }

    public Guild getCurrentGuild() {
        return jda.getGuildById(discordProperty.getServerId());
    }

    public TextChannel getAdminChannel() {
        return jda.getTextChannelById(discordProperty.getAdminChannelId());
    }

    public Member getMemberByUsername(String username) {
        return getCurrentGuild().getMembersByName(username, true).stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DISCORD_MEMBER_NOT_FOUND));
    }
}
