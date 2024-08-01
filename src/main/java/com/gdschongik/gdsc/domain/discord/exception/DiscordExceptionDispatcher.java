package com.gdschongik.gdsc.domain.discord.exception;

import com.gdschongik.gdsc.domain.discord.exception.handler.CommandExceptionHandler;
import com.gdschongik.gdsc.domain.discord.exception.handler.DefaultExceptionHandler;
import com.gdschongik.gdsc.domain.discord.exception.handler.DiscordExceptionHandler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordExceptionDispatcher {

    // TODO: instanceof로 대체

    private static final Map<Class<? extends GenericEvent>, DiscordExceptionHandler> exceptionHandlerMap =
            Map.of(SlashCommandInteractionEvent.class, new CommandExceptionHandler());

    private static final DefaultExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler();

    public void dispatch(Exception exception, Object context) {
        log.error("DiscordException: {}", exception.getMessage());
        DiscordExceptionHandler exceptionHandler =
                exceptionHandlerMap.getOrDefault(context.getClass(), defaultExceptionHandler);
        exceptionHandler.handle(exception, context);
    }
}
