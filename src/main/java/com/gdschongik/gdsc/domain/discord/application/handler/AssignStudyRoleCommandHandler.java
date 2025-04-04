package com.gdschongik.gdsc.domain.discord.application.handler;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.domain.discord.application.CommonDiscordService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignStudyRoleCommandHandler implements DiscordEventHandler {

    private final CommonDiscordService commonDiscordService;

    @Override
    public void delegate(GenericEvent genericEvent) {
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;
        event.deferReply(true).setContent(DEFER_MESSAGE_ASSIGN_STUDY_ROLE).queue();

        String currentMemberDiscordUsername = event.getUser().getName();
        String studyTitle =
                Objects.requireNonNull(event.getOption(OPTION_NAME_STUDY_TITLE)).getAsString();
        Integer academicYear =
                Objects.requireNonNull(event.getOption(OPTION_NAME_STUDY_YEAR)).getAsInt();
        String semester = Objects.requireNonNull(event.getOption(OPTION_NAME_STUDY_SEMESTER))
                .getAsString();

        commonDiscordService.assignDiscordStudyRole(currentMemberDiscordUsername, studyTitle, academicYear, semester);

        event.getHook()
                .sendMessage(REPLY_MESSAGE_ASSIGN_STUDY_ROLE)
                .setEphemeral(true)
                .queue();
    }
}
