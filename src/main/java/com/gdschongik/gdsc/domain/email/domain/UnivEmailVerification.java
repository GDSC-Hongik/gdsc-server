package com.gdschongik.gdsc.domain.email.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@RedisHash(value = "univEmailVerification")
public class UnivEmailVerification {

    @Id
    private String verificationCode;

    private String univEmail;

    private Long memberId;

    @TimeToLive
    private long timeToLiveInSeconds;
}
