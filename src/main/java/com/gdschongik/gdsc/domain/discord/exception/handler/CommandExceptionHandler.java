package com.gdschongik.gdsc.domain.discord.exception.handler;

import com.gdschongik.gdsc.domain.discord.exception.DiscordExceptionMessageGenerator;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;

public class CommandExceptionHandler implements DiscordExceptionHandler {

    @Override
    public void handle(Exception exception, Object context) {
        GenericCommandInteractionEvent event = (GenericCommandInteractionEvent) context;
        String message = DiscordExceptionMessageGenerator.generate(exception);
        event.getHook().sendMessage(message).setEphemeral(true).queue();
    }
}
