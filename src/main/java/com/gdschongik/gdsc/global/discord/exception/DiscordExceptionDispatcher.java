package com.gdschongik.gdsc.global.discord.exception;

import com.gdschongik.gdsc.global.discord.exception.handler.CommandExceptionHandler;
import com.gdschongik.gdsc.global.discord.exception.handler.DefaultExceptionHandler;
import com.gdschongik.gdsc.global.discord.exception.handler.DiscordExceptionHandler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordExceptionDispatcher {

    private static final Map<Class<? extends GenericEvent>, DiscordExceptionHandler> exceptionHandlerMap =
            Map.of(GenericCommandInteractionEvent.class, new CommandExceptionHandler());

    private static final DefaultExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler();

    public void dispatch(Exception exception, Object context) {
        log.error("DiscordException: {}", exception.getMessage());
        DiscordExceptionHandler exceptionHandler =
                exceptionHandlerMap.getOrDefault(context.getClass(), defaultExceptionHandler);
        exceptionHandler.handle(exception, context);
    }
}
