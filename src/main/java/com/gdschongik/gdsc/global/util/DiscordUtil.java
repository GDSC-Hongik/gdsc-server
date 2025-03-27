package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.property.DiscordProperty;
import java.util.Optional;
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
                .orElseThrow(() -> new CustomException(DISCORD_ROLE_NOT_FOUND));
    }

    public Role findRoleById(String roleId) {
        return Optional.ofNullable(jda.getRoleById(roleId))
                .orElseThrow(() -> new CustomException(DISCORD_ROLE_NOT_FOUND));
    }

    public Guild getCurrentGuild() {
        return jda.getGuildById(discordProperty.getServerId());
    }

    public TextChannel getAdminChannel() {
        return jda.getTextChannelById(discordProperty.getAdminChannelId());
    }

    public Optional<Member> getOptionalMemberByUsername(String username) {
        return getCurrentGuild().getMembersByName(username, true).stream().findFirst();
    }

    public Member getMemberById(String discordId) {
        return Optional.ofNullable(getCurrentGuild().getMemberById(discordId))
                .orElseThrow(() -> new CustomException(DISCORD_MEMBER_NOT_FOUND));
    }

    public String getMemberIdByUsername(String username) {
        return getOptionalMemberByUsername(username)
                .orElseThrow(() -> new CustomException(DISCORD_MEMBER_NOT_FOUND))
                .getId();
    }

    public void addStudyRoleToMember(String studyDiscordRoleId, String memberDiscordId) {
        Guild guild = getCurrentGuild();
        Member member = getMemberById(memberDiscordId);
        Role studyRole = findRoleById(studyDiscordRoleId);

        guild.addRoleToMember(member, studyRole).queue();
    }

    public void removeStudyRoleFromMember(String studyDiscordRoleId, String memberDiscordId) {
        Guild guild = getCurrentGuild();
        Member member = getMemberById(memberDiscordId);
        Role studyRole = findRoleById(studyDiscordRoleId);

        guild.removeRoleFromMember(member, studyRole).queue();
    }
}
