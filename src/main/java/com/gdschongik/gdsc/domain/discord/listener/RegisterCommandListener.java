package com.gdschongik.gdsc.domain.discord.listener;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;
import com.gdschongik.gdsc.global.discord.Listener;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

@Listener
@RequiredArgsConstructor
public class RegisterCommandListener extends ListenerAdapter {

    private final OnboardingDiscordService onboardingDiscordService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("register")) {
            TextInput input = TextInput.create("code", "인증코드를 입력해주세요.", TextInputStyle.SHORT)
                    .setPlaceholder("발급받은 4자리 인증코드")
                    .setMinLength(4)
                    .setMaxLength(4)
                    .build();

            Modal modal = Modal.create("registerModal", "디스코드 연동 인증코드 입력")
                    .addComponents(ActionRow.of(input))
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("registerModal")) {
            String code = Objects.requireNonNull(event.getValue("code")).getAsString();
            onboardingDiscordService.verifyDiscordCode(event.getUser().getName(), code);
        }
    }
}
