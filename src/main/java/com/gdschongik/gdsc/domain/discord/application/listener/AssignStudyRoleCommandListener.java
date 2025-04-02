package com.gdschongik.gdsc.domain.discord.application.listener;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.COMMAND_NAME_ASSIGN_STUDY_ROLE;

import com.gdschongik.gdsc.domain.discord.application.handler.AssignStudyRoleCommandHandler;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Listener
@RequiredArgsConstructor
public class AssignStudyRoleCommandListener extends ListenerAdapter {

    private final AssignStudyRoleCommandHandler assignStudyRoleCommandHandler;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME_ASSIGN_STUDY_ROLE)) {
            assignStudyRoleCommandHandler.delegate(event);
        }
    }
}
