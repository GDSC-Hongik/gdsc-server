package com.gdschongik.gdsc.global.util.email;

import static com.gdschongik.gdsc.global.common.constant.EmailConstant.SENDER_ADDRESS;
import static com.gdschongik.gdsc.global.common.constant.EmailConstant.SENDER_PERSONAL;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaEmailSender implements MailSender {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String recipient, String subject, String content) {
        MimeMessage message = writeMimeMessage(recipient, subject, content);
        javaMailSender.send(message);
    }

    private MimeMessage writeMimeMessage(String recipient, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        addRecipients(message, recipient);
        setMessageAttributes(message, subject, content);
        return message;
    }

    private void addRecipients(MimeMessage message, String recipient) {
        try {
            message.addRecipients(RecipientType.TO, recipient);
        } catch (MessagingException e) {
            throwCustomExceptionWithAdditionalMessage(ErrorCode.MESSAGING_EXCEPTION, e.getMessage());
        }
    }

    private void setMessageAttributes(MimeMessage message, String subject, String content) {
        try {
            message.setSubject(subject);
            message.setText(content, "utf-8", "html");
            message.setFrom(getInternetAddress());
        } catch (MessagingException e) {
            throwCustomExceptionWithAdditionalMessage(ErrorCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    private InternetAddress getInternetAddress() throws MessagingException {
        try {
            return new InternetAddress(SENDER_ADDRESS, SENDER_PERSONAL);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new MessagingException("UnsupportedEncodingException");
        }
    }

    private void throwCustomExceptionWithAdditionalMessage(ErrorCode errorCode, String additionalMessage) {
        String errorMessage = errorCode.getMessage() + " : " + additionalMessage;
        throw new CustomException(errorCode, errorMessage);
    }
}
