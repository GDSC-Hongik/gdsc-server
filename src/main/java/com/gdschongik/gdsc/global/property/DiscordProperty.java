package com.gdschongik.gdsc.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "discord")
public class DiscordProperty {

    private final String token;
    private final String serverId;
    private final String commandChannelId;
    private final String adminChannelId;
}
