package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;
import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.CommonDiscordService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordIdBatchCommandHandler implements DiscordEventHandler {

    private final CommonDiscordService commonDiscordService;

    @Override
    public void delegate(GenericEvent genericEvent) {
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;
        event.deferReply(true).setContent(DEFER_MESSAGE_BATCH_DISCORD_ID).queue();

        String discordUsername = event.getUser().getName();
        commonDiscordService.checkPermissionForCommand(discordUsername);
        commonDiscordService.batchDiscordId(VERIFIED);

        event.getHook()
                .sendMessage(REPLY_MESSAGE_BATCH_DISCORD_ID)
                .setEphemeral(true)
                .queue();
    }
}
