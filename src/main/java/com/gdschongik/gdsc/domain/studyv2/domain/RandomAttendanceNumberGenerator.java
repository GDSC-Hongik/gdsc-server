package com.gdschongik.gdsc.domain.studyv2.domain;

import java.security.SecureRandom;
import lombok.SneakyThrows;

/**
 * 네 자리의 랜덤한 출석번호를 생성합니다.
 */
public class RandomAttendanceNumberGenerator implements AttendanceNumberGenerator {

    @Override
    @SneakyThrows
    public int generate() {
        return SecureRandom.getInstanceStrong().ints(1000, 10000).findFirst().orElseThrow();
    }
}
