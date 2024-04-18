package com.gdschongik.gdsc.domain.discord.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.handler.IssuingCodeCommandHandler;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Listener
@RequiredArgsConstructor
public class IssuingCodeCommandListener extends ListenerAdapter {

    private final IssuingCodeCommandHandler issuingCodeCommandHandler;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME_ISSUING_CODE)) {
            issuingCodeCommandHandler.delegate(event);
        }
    }
}
