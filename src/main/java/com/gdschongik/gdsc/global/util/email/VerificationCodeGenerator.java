package com.gdschongik.gdsc.global.util.email;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeGenerator {

    private static final int VERIFICATION_CODE_LENGTH = 16;
    private static final char RANGE_START_CHAR = '0';
    private static final char RANGE_END_CHAR = 'z';

    public String generate() {
        return RandomStringUtils.random(VERIFICATION_CODE_LENGTH, RANGE_START_CHAR, RANGE_END_CHAR, true, true);
    }
}
