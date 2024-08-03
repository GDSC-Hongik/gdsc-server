package com.gdschongik.gdsc.config;

import com.gdschongik.gdsc.global.config.RedisConfig;
import com.gdschongik.gdsc.global.property.RedisProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@EnableConfigurationProperties({RedisProperty.class})
@Import({RedisConfig.class})
public class TestRedisConfig {}
