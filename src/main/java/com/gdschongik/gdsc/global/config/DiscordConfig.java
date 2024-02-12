package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.DiscordProperty;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DiscordConfig {

    private final DiscordProperty discordProperty;

    @Bean
    public JDA jda() {
        return JDABuilder.createDefault(discordProperty.getToken())
                .setActivity(Activity.playing("테스트"))
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
    }

    @Bean
    public ListenerBeanPostProcessor listenerBeanPostProcessor(JDA jda) {
        return new ListenerBeanPostProcessor(jda);
    }
}
