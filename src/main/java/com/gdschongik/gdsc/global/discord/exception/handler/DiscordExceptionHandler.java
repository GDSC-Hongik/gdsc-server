package com.gdschongik.gdsc.global.discord.exception.handler;

public interface DiscordExceptionHandler {

    void handle(Exception exception, Object context);
}
