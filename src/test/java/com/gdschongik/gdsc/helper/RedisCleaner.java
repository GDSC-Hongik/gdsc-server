package com.gdschongik.gdsc.helper;

import com.gdschongik.gdsc.global.property.RedisProperty;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCleaner implements InitializingBean {
    private final RedisProperty redisProperty;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> commands;

    @Override
    public void afterPropertiesSet() {
        RedisURI redisUri = RedisURI.Builder.redis(redisProperty.getHost(), redisProperty.getPort())
                .build();
        redisClient = RedisClient.create(redisUri);
        connection = redisClient.connect();
        commands = connection.sync();
    }

    public void execute() {
        commands.flushdb();
    }
}
