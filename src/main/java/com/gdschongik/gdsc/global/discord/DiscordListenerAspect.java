package com.gdschongik.gdsc.global.discord;

import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DiscordListenerAspect {

    @Around(
            "execution(* com.gdschongik.gdsc.domain.discord.handler.DiscordEventHandler.delegate(*)) && args(genericEvent)")
    public Object doAround(ProceedingJoinPoint joinPoint, GenericEvent genericEvent) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("DiscordListenerAspect : {}", e.getMessage(), e);
            event.reply(e.getMessage()).setEphemeral(true).queue();
            return null;
        }
    }
}
