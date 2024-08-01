package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.global.util.DiscordUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NonCommandHandler implements DiscordEventHandler {

    private final DiscordUtil discordUtil;

    @Override
    public void delegate(GenericEvent genericEvent) {
        MessageReceivedEvent event = (MessageReceivedEvent) genericEvent;
        Role adminRole = discordUtil.findRoleByName(ADMIN_ROLE_NAME);

        Member member = Objects.requireNonNull(event.getMember());

        if (member.getUser().isBot()) {
            return;
        }

        if (member.getRoles().contains(adminRole)) {
            return;
        }

        event.getMessage().delete().queue();
    }
}
