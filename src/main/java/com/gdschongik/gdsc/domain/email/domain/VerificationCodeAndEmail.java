package com.gdschongik.gdsc.domain.email.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@RedisHash(value = "verificationCodeAndEmail")
public class VerificationCodeAndEmail {

    @Id
    private String verificationCode;

    private String email;

    private Long memberId;

    @TimeToLive
    private long timeToLiveInSeconds;
}
