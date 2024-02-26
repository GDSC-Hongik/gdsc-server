package com.gdschongik.gdsc.global.util.email;

public interface MailSender {

    void send(String recipient, String subject, String content);
}
