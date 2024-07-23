package com.gdschongik.gdsc.domain.email.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "univEmailVerification")
public class UnivEmailVerification {

    @Id
    private Long memberId;

    private String verificationToken;

    @TimeToLive
    private long ttl;

    @Builder(access = AccessLevel.PRIVATE)
    private UnivEmailVerification(Long memberId, String verificationToken, long ttl) {
        this.memberId = memberId;
        this.verificationToken = verificationToken;
        this.ttl = ttl;
    }

    public static UnivEmailVerification of(Long memberId, String verificationToken, long ttl) {
        return UnivEmailVerification.builder()
                .memberId(memberId)
                .verificationToken(verificationToken)
                .ttl(ttl)
                .build();
    }
}
