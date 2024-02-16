package com.gdschongik.gdsc.domain.discord.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordVerificationCodeResponse;
import com.gdschongik.gdsc.global.discord.Listener;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Listener
@RequiredArgsConstructor
public class RegisterCommandListener extends ListenerAdapter {

    private final OnboardingDiscordService onboardingDiscordService;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME_ISSUING_CODE)) {
            event.deferReply()
                    .setEphemeral(true)
                    .setContent(DEFER_MESSAGE_ISSUING_CODE)
                    .queue();

            String discordUsername = event.getUser().getName();
            DiscordVerificationCodeResponse verificationCode =
                    onboardingDiscordService.createVerificationCode(discordUsername);

            String message = String.format(
                    REPLY_MESSAGE_ISSUING_CODE,
                    verificationCode.code(),
                    verificationCode.ttl().toMinutes());

            event.getHook().sendMessage(message).setEphemeral(true).queue();
        }
    }
}
