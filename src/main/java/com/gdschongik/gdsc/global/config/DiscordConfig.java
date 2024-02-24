package com.gdschongik.gdsc.global.config;

import static com.gdschongik.gdsc.global.common.constant.DiscordConstant.*;

import com.gdschongik.gdsc.global.discord.ListenerBeanPostProcessor;
import com.gdschongik.gdsc.global.property.DiscordProperty;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DiscordConfig {

    private final DiscordProperty discordProperty;

    @Bean
    @SneakyThrows
    @ConditionalOnProperty(value = "discord.enabled", havingValue = "true", matchIfMissing = true)
    public JDA jda() {
        JDA jda = JDABuilder.createDefault(discordProperty.getToken())
                .setActivity(Activity.playing("테스트"))
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        // TODO: ready 후 리스너 빈이 초기화되면 이벤트 수신 불가하므로, @PostConstruct로 ReadyListener 사용할 수 있도록 변경
        Objects.requireNonNull(jda.awaitReady().getGuildById(discordProperty.getServerId()))
                .updateCommands()
                .addCommands(Commands.slash(COMMAND_NAME_ISSUING_CODE, COMMAND_DESCRIPTION_ISSUING_CODE))
                .queue();

        return jda;
    }

    @Bean
    @ConditionalOnBean(JDA.class)
    public ListenerBeanPostProcessor listenerBeanPostProcessor(JDA jda) {
        return new ListenerBeanPostProcessor(jda);
    }
}
