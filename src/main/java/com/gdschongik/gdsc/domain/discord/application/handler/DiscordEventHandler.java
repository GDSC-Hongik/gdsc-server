package com.gdschongik.gdsc.domain.discord.application.handler;

import net.dv8tion.jda.api.events.GenericEvent;

public interface DiscordEventHandler {

    // TODO: GenericEvent에 대한 어댑터 추가
    void delegate(GenericEvent genericEvent);
}
