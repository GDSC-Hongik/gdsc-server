package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.EmailProperty;
import com.gdschongik.gdsc.global.property.EmailProperty.Gmail;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class JavaMailSenderConfig {

    private final EmailProperty emailProperty;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailProperty.getHost());
        javaMailSender.setPort(emailProperty.getPort());
        javaMailSender.setDefaultEncoding(emailProperty.getEncoding());
        javaMailSender.setProtocol(emailProperty.getProtocol());

        setGmailProperty(javaMailSender);
        setJavaMailProperties(javaMailSender);
        return javaMailSender;
    }

    private void setGmailProperty(JavaMailSenderImpl javaMailSender) {
        Gmail gmail = emailProperty.getGmail();
        javaMailSender.setUsername(gmail.loginEmail());
        javaMailSender.setPassword(gmail.password());
    }

    private void setJavaMailProperties(JavaMailSenderImpl javaMailSender) {
        Properties properties = new Properties();
        properties.putAll(emailProperty.getJavaMailProperty());
        javaMailSender.setJavaMailProperties(properties);
    }
}
