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
        } catch (CustomException e) {
            log.error("DiscordException: {}", e.getMessage());
            GenericCommandInteractionEvent event = (GenericCommandInteractionEvent) genericEvent;
            event.getHook().sendMessage(e.getMessage()).setEphemeral(true).queue();
            return null;
        } catch (Exception e) {
            log.error("DiscordException: {}", e.getMessage());
            GenericCommandInteractionEvent event = (GenericCommandInteractionEvent) genericEvent;
            event.getHook().sendMessage("알 수 없는 오류가 발생했습니다.").setEphemeral(true).queue();
            return null;
        }
    }
}
