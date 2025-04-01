package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvanceFailedMemberCommandHandler implements DiscordEventHandler {

    private final AdminMemberService adminMemberService;

    @Override
    public void delegate(GenericEvent genericEvent) {
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;
        event.deferReply(true).setContent(DEFER_MESSAGE_ADVANCE_FAILED_MEMBER).queue();

        String discordUsername = event.getUser().getName();
        adminMemberService.advanceAllAdvanceFailedMembersToRegular(discordUsername);

        event.getHook()
                .sendMessage(REPLY_MESSAGE_ADVANCE_FAILED_MEMBER)
                .setEphemeral(true)
                .queue();
    }
}
