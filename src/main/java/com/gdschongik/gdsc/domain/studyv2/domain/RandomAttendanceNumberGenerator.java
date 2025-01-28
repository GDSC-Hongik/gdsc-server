package com.gdschongik.gdsc.domain.studyv2.domain;

import java.security.SecureRandom;
import lombok.SneakyThrows;

/**
 * 네 자리의 랜덤한 출석번호를 생성합니다.
 */
public class RandomAttendanceNumberGenerator implements AttendanceNumberGenerator {

    public static final int MIN_ORIGIN = 1000;
    public static final int MAX_BOUND = 10000;

    @Override
    @SneakyThrows
    public int generate() {
        return SecureRandom.getInstanceStrong()
                .ints(MIN_ORIGIN, MAX_BOUND)
                .findFirst()
                .orElseThrow();
    }
}
