package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.DelegateMemberDiscordEventHandler;
import com.gdschongik.gdsc.domain.member.domain.event.MemberAdvancedToRegularEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelegateMemberDiscordEventListener {

    private final DelegateMemberDiscordEventHandler delegateMemberDiscordEventHandler;

    @ApplicationModuleListener
    public void delegateMemberDiscordEvent(MemberAdvancedToRegularEvent event) {
        log.info("[DelegateMemberDiscordEventListener] 정회원 승급 이벤트 수신: memberId={}", event.memberId());
        delegateMemberDiscordEventHandler.delegate(event);
    }
}
