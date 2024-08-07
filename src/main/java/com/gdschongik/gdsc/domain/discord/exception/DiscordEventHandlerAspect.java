package com.gdschongik.gdsc.domain.discord.exception;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DiscordEventHandlerAspect {

    private final DiscordExceptionDispatcher discordExceptionDispatcher;

    @Around(
            "execution(* com.gdschongik.gdsc.domain.discord.application.handler.DiscordEventHandler.delegate(*)) && args(genericEvent)")
    public Object doAround(ProceedingJoinPoint joinPoint, GenericEvent genericEvent) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            discordExceptionDispatcher.dispatch(e, genericEvent);
            return null;
        }
    }
}
