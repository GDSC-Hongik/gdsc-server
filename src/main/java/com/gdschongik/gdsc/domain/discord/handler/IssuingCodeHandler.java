package com.gdschongik.gdsc.domain.discord.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordVerificationCodeResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssuingCodeHandler implements DiscordEventHandler {

    private final OnboardingDiscordService onboardingDiscordService;

    @Override
    public void delegate(GenericEvent genericEvent) {
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;

        event.deferReply()
                .setEphemeral(true)
                .setContent(DEFER_MESSAGE_ISSUING_CODE)
                .queue();

        String discordUsername = event.getUser().getName();
        DiscordVerificationCodeResponse verificationCode =
                onboardingDiscordService.createVerificationCode(discordUsername);

        if (discordUsername != null) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        String message = String.format(
                REPLY_MESSAGE_ISSUING_CODE,
                verificationCode.code(),
                verificationCode.ttl().toMinutes());

        event.getHook().sendMessage(message).setEphemeral(true).queue();
    }
}
