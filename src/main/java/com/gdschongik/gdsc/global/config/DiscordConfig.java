package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.DiscordProperty;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DiscordConfig {

    private final DiscordProperty discordProperty;

    @Bean
    public JDA discord() {
        return JDABuilder.createDefault(discordProperty.getToken()).build();
    }
}
