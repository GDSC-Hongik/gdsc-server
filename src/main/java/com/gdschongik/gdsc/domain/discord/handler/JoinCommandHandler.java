package com.gdschongik.gdsc.domain.discord.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JoinCommandHandler implements DiscordEventHandler {

	private final OnboardingDiscordService onboardingDiscordService;

	@Override
	public void delegate(GenericEvent genericEvent) {
		SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;

		event.deferReply()
				.setEphemeral(true)
				.setContent(DEFER_MESSAGE_JOIN)
				.queue();

		String discordUsername = event.getUser().getName();
		onboardingDiscordService.checkDiscordRoleAssignable(discordUsername);

		// TODO: 커뮤니티 멤버 역할 부여

		event.getHook().sendMessage(REPLY_MESSAGE_JOIN).setEphemeral(true).queue();
	}
}
