package com.gdschongik.gdsc.domain.discord.exception;

import com.gdschongik.gdsc.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SpringEventHandlerAspect {

    private final DiscordUtil discordUtil;

	@Around(
            "execution(* com.gdschongik.gdsc.domain.discord.application.handler.SpringEventHandler.delegate(*)) && args(ignoredContext)")
    public Object doAround(ProceedingJoinPoint joinPoint, Object ignoredContext) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            sendErrorMessageToDiscord(e);
            return null;
        }
    }

    private void sendErrorMessageToDiscord(Exception e) {
        TextChannel channel = discordUtil.getAdminChannel();
        channel.sendMessage(e.getMessage()).queue();
    }
}
