package com.gdschongik.gdsc.domain.discord.application.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.handler.DiscordIdBatchCommandHandler;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Listener
@RequiredArgsConstructor
public class DiscordIdBatchCommandListener extends ListenerAdapter {

    private final DiscordIdBatchCommandHandler discordIdBatchCommandHandler;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME_BATCH_DISCORD_ID)) {
            discordIdBatchCommandHandler.delegate(event);
        }
    }
}
