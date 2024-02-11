package com.gdschongik.gdsc.domain.integration;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RequiredArgsConstructor
@RedisHash(value = "emailVerificationCode")
public class EmailVerificationCode {

    @Id
    private String email;

    private String verificationCode;

    @TimeToLive
    private static final long TIME_TO_LIVE = Duration.ofMinutes(10)
        .toSeconds();
}
