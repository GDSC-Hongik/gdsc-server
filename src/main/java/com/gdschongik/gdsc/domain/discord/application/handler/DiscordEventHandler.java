package com.gdschongik.gdsc.domain.discord.application.handler;

import net.dv8tion.jda.api.events.GenericEvent;

public interface DiscordEventHandler {

    void delegate(GenericEvent genericEvent);
}
