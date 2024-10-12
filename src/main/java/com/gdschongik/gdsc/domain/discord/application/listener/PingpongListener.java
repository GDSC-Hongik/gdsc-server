package com.gdschongik.gdsc.domain.discord.application.listener;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.*;

import com.gdschongik.gdsc.global.annotation.ConditionalOnProfile;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
@ConditionalOnProfile({LOCAL, DEV})
@Listener
public class PingpongListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = event.getMessage();
        String content = message.getContentRaw(); // get only textual content create message

        log.info("Message create {} in {}: {}", author.getName(), channel.getName(), message.getContentDisplay());

        if (author.isBot()) return;

        if (content.equals("!ping")) {
            channel.sendMessage("Pong!").queue();
        }
    }
}
