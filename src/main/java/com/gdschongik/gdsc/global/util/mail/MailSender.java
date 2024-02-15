package com.gdschongik.gdsc.global.util.mail;

public interface MailSender {

    void send(String recipient, String subject, String content);
}
