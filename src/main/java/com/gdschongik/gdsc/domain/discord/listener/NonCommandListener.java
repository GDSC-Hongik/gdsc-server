package com.gdschongik.gdsc.domain.discord.listener;

import com.gdschongik.gdsc.domain.discord.handler.NonCommandHandler;
import com.gdschongik.gdsc.global.discord.Listener;
import com.gdschongik.gdsc.global.property.DiscordProperty;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Listener
@RequiredArgsConstructor
public class NonCommandListener extends ListenerAdapter {

    private final DiscordProperty discordProperty;
    private final NonCommandHandler nonCommandHandler;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String eventChannelId = event.getChannel().getId();

        if (eventChannelId.equals(discordProperty.getCommandChannelId())) {
            nonCommandHandler.delegate(event);
        }
    }
}
