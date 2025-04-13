package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.property.DiscordProperty;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@RequiredArgsConstructor
public class DiscordUtil {

    public static final String IMAGE_GENERATOR_URL = "https://image.wawoo.dev/api/v1/study-announcement";

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

    public void addRoleToMemberById(String discordRoleId, String memberDiscordId) {
        Guild guild = getCurrentGuild();
        Member member = getMemberById(memberDiscordId);
        Role studyRole = findRoleById(discordRoleId);

        guild.addRoleToMember(member, studyRole).queue();
    }

    public void removeRoleFromMemberById(String discordRoleId, String memberDiscordId) {
        Guild guild = getCurrentGuild();
        Member member = getMemberById(memberDiscordId);
        Role studyRole = findRoleById(discordRoleId);

        guild.removeRoleFromMember(member, studyRole).queue();
    }

    public void sendStudyAnnouncementToChannel(
            String channelId,
            String discordRoleId,
            String studyName,
            String title,
            String link,
            LocalDateTime createdAt) {

        TextChannel channel = Optional.ofNullable(jda.getTextChannelById(channelId))
                .orElseThrow(() -> new CustomException(DISCORD_CHANNEL_NOT_FOUND));

        String studyRoleMention = findRoleById(discordRoleId).getAsMention();

        String theme = "indigo"; // 디폴트 값

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("[" + title + "]")
                .appendDescription(studyRoleMention + "\n\n")
                .appendDescription(studyName + " 공지가 업로드 되었어요.\n")
                .appendDescription("공지는 [와우클래스](<https://study.wawoo.dev/landing>)에서도 확인 가능해요.\n")
                .appendDescription(String.format("## [► 공지 확인하러 가기](<%s>)\n", link))
                .setTimestamp(convertKstToUtc(createdAt))
                .setImage(buildImageUrl(studyName, title, createdAt, theme))
                .build();

        channel.sendMessageEmbeds(embed).queue();
    }

    private Instant convertKstToUtc(LocalDateTime kstCreatedAt) {
        return kstCreatedAt
                .atZone(ZoneId.of("Asia/Seoul"))
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toInstant();
    }

    private String buildImageUrl(String studyName, String title, LocalDateTime dateTime, String theme) {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String encodedStudy = URLEncoder.encode(studyName, StandardCharsets.UTF_8);

        StringBuilder urlBuilder = new StringBuilder(IMAGE_GENERATOR_URL)
                .append("?title=")
                .append(encodedTitle)
                .append("&study=")
                .append(encodedStudy);

        if (dateTime != null) {
            String encodedDate = URLEncoder.encode(dateTime.toString(), StandardCharsets.UTF_8);
            urlBuilder.append("&date=").append(encodedDate);
        }

        if (theme != null && !theme.isEmpty()) {
            String encodedTheme = URLEncoder.encode(theme, StandardCharsets.UTF_8);
            urlBuilder.append("&theme=").append(encodedTheme);
        }

        return urlBuilder.toString();
    }
}
