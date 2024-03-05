package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.BasicAuthProperty;
import com.gdschongik.gdsc.global.property.DiscordProperty;
import com.gdschongik.gdsc.global.property.JwtProperty;
import com.gdschongik.gdsc.global.property.RedisProperty;
import com.gdschongik.gdsc.global.property.email.EmailProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
    JwtProperty.class,
    RedisProperty.class,
    BasicAuthProperty.class,
    DiscordProperty.class,
    EmailProperty.class
})
@Configuration
public class PropertyConfig {}
