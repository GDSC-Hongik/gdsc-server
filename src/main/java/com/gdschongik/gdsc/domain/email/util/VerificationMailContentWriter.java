package com.gdschongik.gdsc.domain.email.util;

import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class VerificationMailContentWriter {

    private static final String NOTIFICATION_MESSAGE = "<div style='margin:20px;'>"
            + "<h1> 안녕하세요 GDSC Hongik 재학생 인증 메일입니다. </h1> <br>"
            + "<h3> 아래의 링크를 %d분 안에 클릭해주세요. </h3> <br>"
            + "<h3> 감사합니다. </h3> <br>"
            + "CODE : <strong>";

    public String write(String verificationLink, Duration codeAliveTime) {
        return String.format(NOTIFICATION_MESSAGE, codeAliveTime.toMinutes()) + verificationLink;
    }
}
