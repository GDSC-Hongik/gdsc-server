package com.gdschongik.gdsc.domain.integration;

public interface MailSender {

    void send(String recipient, String content);
}
