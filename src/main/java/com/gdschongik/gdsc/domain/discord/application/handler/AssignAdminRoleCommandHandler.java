package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignAdminRoleCommandHandler implements DiscordEventHandler {

    private final AdminMemberService adminMemberService;

    @Override
    public void delegate(GenericEvent genericEvent) {
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;
        event.deferReply(true).setContent(DEFER_MESSAGE_ASSIGN_ADMIN_ROLE).queue();

        String discordUsername = event.getUser().getName();
        String studentId = Objects.requireNonNull(event.getOption(OPTION_NAME_ASSIGN_ADMIN_ROLE))
                .getAsString();

        adminMemberService.assignAdminRole(discordUsername, studentId);

        event.getHook()
                .sendMessage(REPLY_MESSAGE_ASSIGN_ADMIN_ROLE)
                .setEphemeral(true)
                .queue();
    }
}
