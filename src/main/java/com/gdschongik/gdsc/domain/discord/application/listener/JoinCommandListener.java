package com.gdschongik.gdsc.domain.discord.application.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.handler.JoinCommandHandler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Deprecated
@Listener
@RequiredArgsConstructor
public class JoinCommandListener extends ListenerAdapter {

    private final JoinCommandHandler joinCommandHandler;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME_JOIN)) {
            joinCommandHandler.delegate(event);
        }
    }
}
