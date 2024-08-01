package com.gdschongik.gdsc.domain.discord.exception;

import com.gdschongik.gdsc.global.exception.CustomException;

public class DiscordExceptionMessageGenerator {

    public static String generate(Exception exception) {
        if (exception instanceof CustomException) {
            return exception.getMessage();
        }

        return "알 수 없는 오류가 발생했습니다.";
    }
}
