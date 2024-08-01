package com.gdschongik.gdsc.domain.discord.exception.handler;

public interface DiscordExceptionHandler {

    void handle(Exception exception, Object context);
}
