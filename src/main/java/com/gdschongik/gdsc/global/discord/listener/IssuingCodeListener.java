package com.gdschongik.gdsc.global.discord.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.handler.IssuingCodeHandler;
import com.gdschongik.gdsc.global.discord.Listener;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Listener
@RequiredArgsConstructor
public class IssuingCodeListener extends ListenerAdapter {

    private final IssuingCodeHandler issuingCodeHandler;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME_ISSUING_CODE)) {
            issuingCodeHandler.delegate(event);
        }
    }
}
