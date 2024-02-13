package com.gdschongik.gdsc.domain.integration;

import static com.gdschongik.gdsc.domain.integration.VerificationMailConstant.MESSAGE_SUBJECT;
import static com.gdschongik.gdsc.domain.integration.VerificationMailConstant.SENDER_ADDRESS;
import static com.gdschongik.gdsc.domain.integration.VerificationMailConstant.SENDER_PERSONAL;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaMailSender implements MailSender {

    private final org.springframework.mail.javamail.JavaMailSender javaMailSender;

    @Override
    public void send(String recipient, String content) {
        MimeMessage message = writeMimeMessage(recipient, content);
        javaMailSender.send(message);
    }

    private MimeMessage writeMimeMessage(String recipient, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        addRecipients(message, recipient);
        setMessageAttributes(message, content);
        return message;
    }

    private void addRecipients(MimeMessage message, String recipient) {
        try {
            message.addRecipients(RecipientType.TO, recipient);
        } catch (MessagingException e) {
            throwCustomExceptionWithAdditionalMessage(
                ErrorCode.MESSAGING_EXCEPTION, e.getMessage());
        }
    }

    private void setMessageAttributes(MimeMessage message, String content) {
        try {
            message.setSubject(MESSAGE_SUBJECT);
            message.setText(content, "utf-8", "html");
            message.setFrom(getInternetAddress());
        } catch (MessagingException e) {
            throwCustomExceptionWithAdditionalMessage(
                ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private InternetAddress getInternetAddress() throws MessagingException {
        try {
            return new InternetAddress(SENDER_ADDRESS, SENDER_PERSONAL);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new MessagingException("UnsupportedEncodingException");
        }
    }

    private void throwCustomExceptionWithAdditionalMessage(ErrorCode errorCode,
        String additionalMessage) {
        String errorMessage = errorCode.getMessage() + " : " + additionalMessage;
        throw new CustomException(errorCode, errorMessage);
    }
}
