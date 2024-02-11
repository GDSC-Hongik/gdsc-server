package com.gdschongik.gdsc.domain.integration;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JavaMailSender implements MailSender {

    private static final String SENDER_PERSONAL = "GDSC Hongik 운영팀";
    private static final String SENDER_ADDRESS = "gdsc.hongik@gmail.com";
    private static final String MESSAGE_SUBJECT = "GDSC Hongik 이메일 인증 코드입니다.";

    private final org.springframework.mail.javamail.JavaMailSender javaMailSender;

    @Override
    public void send(String recipient, String content) {
        try {
            MimeMessage message = writeMimeMessage(recipient, content);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage writeMimeMessage(String recipient, String content)
        throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, recipient);
        message.setSubject(MESSAGE_SUBJECT);
        message.setText(content, "utf-8", "html");
        message.setFrom(getInternetAddress());
        return message;
    }

    private InternetAddress getInternetAddress() {
        try {
            return new InternetAddress(SENDER_ADDRESS, SENDER_PERSONAL);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            return new InternetAddress();
        }
    }
}
