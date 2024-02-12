package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.DiscordProperty;
import com.gdschongik.gdsc.global.property.JwtProperty;
import com.gdschongik.gdsc.global.property.RedisProperty;
import com.gdschongik.gdsc.global.property.SwaggerProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({JwtProperty.class, RedisProperty.class, SwaggerProperty.class, DiscordProperty.class})
@Configuration
public class PropertyConfig {}
