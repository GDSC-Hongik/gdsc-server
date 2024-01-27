package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.JwtProperty;
import com.gdschongik.gdsc.global.property.RedisProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({JwtProperty.class, RedisProperty.class})
@Configuration
public class PropertyConfig {}
