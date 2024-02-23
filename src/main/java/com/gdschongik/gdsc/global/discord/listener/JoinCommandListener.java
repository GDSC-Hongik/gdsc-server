package com.gdschongik.gdsc.global.discord.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.handler.JoinCommandHandler;
import com.gdschongik.gdsc.global.discord.Listener;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
