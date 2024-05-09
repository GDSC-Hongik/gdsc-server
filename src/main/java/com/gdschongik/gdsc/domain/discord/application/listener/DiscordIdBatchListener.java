package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.DiscordIdBatchHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Listener
@RequiredArgsConstructor
public class DiscordIdBatchListener extends ListenerAdapter {

    private final DiscordIdBatchHandler discordIdBatchHandler;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        discordIdBatchHandler.delegate(event);
    }
}
