package com.gdschongik.gdsc.global.discord.listener;

import com.gdschongik.gdsc.domain.discord.handler.NicknameModifyHandler;
import com.gdschongik.gdsc.global.discord.Listener;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Listener
@RequiredArgsConstructor
public class NicknameModifyListener extends ListenerAdapter {

    private final NicknameModifyHandler nicknameModifyHandler;

    @Override
    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
        nicknameModifyHandler.delegate(event);
    }
}
