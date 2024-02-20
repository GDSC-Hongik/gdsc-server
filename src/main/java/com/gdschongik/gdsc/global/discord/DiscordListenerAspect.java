package com.gdschongik.gdsc.global.discord;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class DiscordListenerAspect {

    @Around("execution(public void com.gdschongik.gdsc.domain.discord.listener.*Listener.on*(*)) && args(event)")
    public Object doAround(ProceedingJoinPoint joinPoint, SlashCommandInteractionEvent event) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("DiscordListenerAspect : {}", e.getMessage(), e);
            event.reply(e.getMessage()).setEphemeral(true).queue();
            return null;
        }
    }
}
