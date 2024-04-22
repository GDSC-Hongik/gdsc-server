package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.DiscordClientService;
import com.gdschongik.gdsc.domain.member.domain.MemberGrantEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberGrantEventListener {

    private final DiscordClientService discordClientService;

    @TransactionalEventListener(MemberGrantEvent.class)
    public void handleMemberGrantEvent(MemberGrantEvent event) {
        discordClientService.assignMemberRole(event);
    }
}
