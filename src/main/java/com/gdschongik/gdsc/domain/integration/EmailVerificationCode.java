package com.gdschongik.gdsc.domain.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@RedisHash(value = "emailVerificationCode")
public class EmailVerificationCode {

    @Id
    private String email;

    private String verificationCode;

    @TimeToLive
    private long timeToLiveInSeconds;
}
