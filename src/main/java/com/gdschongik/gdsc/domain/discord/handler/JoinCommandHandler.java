package com.gdschongik.gdsc.domain.discord.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordNicknameResponse;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JoinCommandHandler implements DiscordEventHandler {

    private final OnboardingDiscordService onboardingDiscordService;
    private final DiscordUtil discordUtil;

    @Override
    public void delegate(GenericEvent genericEvent) {
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;

        event.deferReply().setEphemeral(true).setContent(DEFER_MESSAGE_JOIN).queue();

        String discordUsername = event.getUser().getName();
        DiscordNicknameResponse response = onboardingDiscordService.checkDiscordRoleAssignable(discordUsername);

        User user = event.getUser();
        Role role = discordUtil.findRoleByName(MEMBER_ROLE_NAME);
        Guild guild = Objects.requireNonNull(event.getGuild());

        guild.addRoleToMember(user, role).queue();

        event.getHook().sendMessage(REPLY_MESSAGE_JOIN).setEphemeral(true).queue();
    }
}
